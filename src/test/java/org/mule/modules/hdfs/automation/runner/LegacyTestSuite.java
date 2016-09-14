/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.hdfs.automation.runner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.mule.modules.hdfs.automation.functional.legacy.*;

/**
 * Cloud Connector
 *
 * @author MuleSoft, Inc.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        GetMetadataTestCases.class,
        ReadTestCases.class,
        RenameTestCases.class,
        SetOwnerTestCases.class,
        SetPermissionTestCases.class,
        WriteTestCases.class,
        DeleteDirectoryTestCases.class
})
public class LegacyTestSuite {

}
