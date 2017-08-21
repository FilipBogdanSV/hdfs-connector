/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.extension.hdfs.automation.functional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.mule.extension.hdfs.api.FileStatus;
import org.mule.extension.hdfs.util.TestDataBuilder;
import org.mule.extension.hdfs.util.Util;
import org.mule.modules.hdfs.automation.functional.BaseTest;

@SuppressWarnings("unchecked")
public class MakeDirectoriesTestCases extends BaseTest {

    private static final String PARENT_DIRECTORY = "rootDirectory/";
    private static final String NEW_DIRECTORY = "newDirectory";

    @Override
    protected String getConfigFile() {
        return Util.MAKE_DIR_FLOW_PATH;
    }

    @Test
    public void testMakeDirectories() throws Exception {
        flowRunner(Util.FlowNames.MAKE_DIR_FLOW).withVariable("path", PARENT_DIRECTORY + NEW_DIRECTORY)
                .withVariable("permission", "700")
                .run();
        verifyMakeDirectory();
    }

    @After
    public void tearDown() throws Exception {
        flowRunner(Util.FlowNames.DELETE_DIR_FLOW).withVariable("path", PARENT_DIRECTORY)
                .run();
    }

    private void verifyMakeDirectory() throws Exception {

        List<FileStatus> parentDirectoryStatuses = (List<FileStatus>) TestDataBuilder
                .getValue(flowRunner(Util.FlowNames.LIST_STATUS_FLOW).withVariable("path", PARENT_DIRECTORY)
                        .run());

        assertThat(parentDirectoryStatuses, notNullValue());

        assertThat(parentDirectoryStatuses.get(0)
                .getPath(), containsString(NEW_DIRECTORY));
    }
}
