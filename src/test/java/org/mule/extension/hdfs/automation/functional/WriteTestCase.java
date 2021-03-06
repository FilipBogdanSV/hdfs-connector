/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.extension.hdfs.automation.functional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.core.StringContains;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mule.extension.hdfs.api.FileStatus;
import org.mule.extension.hdfs.util.TestConstants;

import org.mule.runtime.api.exception.MuleException;

import org.mule.extension.hdfs.util.TestDataBuilder;

import org.mule.runtime.api.streaming.bytes.CursorStream;
import org.mule.runtime.api.streaming.bytes.CursorStreamProvider;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SuppressWarnings("unchecked")
public class WriteTestCase extends BaseTest {

    private final byte[] writtenData = RandomStringUtils.randomAlphanumeric(20)
            .getBytes();
    private final String FILE_PATH = "hdfs-test-write-file.txt";
    private final String NO_PERMISSION_FILE_PATH = "no-permission-write-file.txt";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    public WriteTestCase(String configuration) {
        super(configuration);
    }

    @Override
    public String getFlowFile() {
        return TestConstants.WRITE_OPERATION_FLOW_PATH;
    }

    @Test
    public void testWriteFlow() throws Exception {

        InputStream payload = new ByteArrayInputStream(writtenData);
        flowRunner(TestConstants.FlowNames.WRITE_FLOW).withVariable("path", FILE_PATH)
                .withPayload(payload)
                .run();

        CursorStream cursor = ((CursorStreamProvider) flowRunner(TestConstants.FlowNames.READ_OP_FLOW).withVariable("path", FILE_PATH)
                .keepStreamsOpen()
                .run()
                .getMessage()
                .getPayload()
                .getValue()).openCursor();

        byte[] actualContent = IOUtils.toByteArray(cursor);
        assertThat("Read content is different from what was written.", actualContent, equalTo(writtenData));

    }

    @Test
    public void testWriteNullPayloadFlow() throws Exception {
        expectedException.expect(MuleException.class);
        expectedException.expectMessage(StringContains.containsString("Payload cannot be null"));

        InputStream payload = null;
        flowRunner(TestConstants.FlowNames.WRITE_FLOW).withVariable("path", FILE_PATH)
                .withPayload(payload)
                .run();
    }

    @Test
    public void testWriteNoPermissionFlow() throws Exception {

        InputStream payload = new ByteArrayInputStream(writtenData);
        flowRunner(TestConstants.FlowNames.WRITE_FLOW).withVariable("path", NO_PERMISSION_FILE_PATH)
                .withVariable("permission", "000")
                .withPayload(payload)
                .run();

        List<FileStatus> listStatus = (List<FileStatus>) TestDataBuilder
                .getValue(flowRunner(TestConstants.FlowNames.LIST_STATUS_FLOW).withVariable("path", NO_PERMISSION_FILE_PATH)
                        .withPayload(payload)
                        .run());

        assertThat("It should be at list one file.", listStatus.size() > 0);
        assertThat("File should have no permission.", listStatus.get(0)
                .getPermission()
                .toString(), equalTo("---------"));
    }
}
