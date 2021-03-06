/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.extension.hdfs.automation.functional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.extension.hdfs.util.TestConstants;
import org.mule.extension.hdfs.util.TestDataBuilder;

public class CopyToLocalFileTestCase extends BaseTest {

    private static final String MYFILE_PATH = "myfile.txt";
    private static final String LOCAL_TARGET_PATH = "src/test/resources/data-sets/myfile.txt";
    private byte[] initialWrittenData;

    public CopyToLocalFileTestCase(String configuration) {
        super(configuration);
    }

    @Override
    public String getFlowFile() {
        return TestConstants.COPY_TO_LOCAL_FLOW_PATH;
    }

    @Before
    public void setUp() throws Exception {
        initialWrittenData = TestDataBuilder.payloadShortString();
        flowRunner(TestConstants.FlowNames.WRITE_FLOW).withVariable("path", MYFILE_PATH)
                .withPayload(new ByteArrayInputStream(initialWrittenData))
                .run();

    }

    @Test
    public void testCopyToLocalFile() throws Exception {
        flowRunner(TestConstants.FlowNames.COPY_TO_LOCAL_FILE_FLOW).withVariable("deleteSource", false)
                .withVariable("useRawLocalFileSystem", false)
                .withVariable("source", MYFILE_PATH)
                .withVariable("destination", LOCAL_TARGET_PATH)
                .run();

        Path localTarget = Paths.get(LOCAL_TARGET_PATH);
        InputStream targetDataStream = Files.newInputStream(localTarget);
        InputStream sourceDataStream = new ByteArrayInputStream(initialWrittenData);
        assertThat(IOUtils.contentEquals(targetDataStream, sourceDataStream), is(true));
    }

    @After
    public void tearDown() throws Exception {
        flowRunner(TestConstants.FlowNames.DELETE_FILE_FLOW).withVariable("path", MYFILE_PATH)
                .run();
        Files.delete(Paths.get(LOCAL_TARGET_PATH));
        Path localTarget = Paths.get(LOCAL_TARGET_PATH);
        Files.delete(localTarget.resolveSibling("." + localTarget.getFileName()
                .toString() + ".crc"));
    }
}
