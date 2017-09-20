/**
 * (c) 2003-2017 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.extension.hdfs.internal.service;

import java.io.InputStream;
import java.util.List;

import org.mule.extension.hdfs.internal.service.dto.FileStatusDTO;
import org.mule.extension.hdfs.internal.service.dto.MetaDataDTO;
import org.mule.extension.hdfs.internal.service.exception.HdfsConnectionException;
import org.mule.extension.hdfs.internal.service.exception.InvalidRequestDataException;
import org.mule.extension.hdfs.internal.service.exception.UnableToRetrieveResponseException;
import org.mule.extension.hdfs.internal.service.exception.UnableToSendRequestException;

public interface HdfsAPIService {

    InputStream read(String path, int bufferSize) throws InvalidRequestDataException, UnableToRetrieveResponseException, UnableToSendRequestException, HdfsConnectionException;

    void create(String path, String permission, boolean overwrite, int bufferSize, int replication, long blockSize, String ownerUserName, String ownerGroupName,
            InputStream payload) throws InvalidRequestDataException, UnableToRetrieveResponseException, UnableToSendRequestException, HdfsConnectionException;

    List<FileStatusDTO> listStatus(String path, String filter)
            throws InvalidRequestDataException, UnableToRetrieveResponseException, UnableToSendRequestException, HdfsConnectionException;

    List<FileStatusDTO> globStatus(String pathPattern, String filter)
            throws InvalidRequestDataException, UnableToRetrieveResponseException, UnableToSendRequestException, HdfsConnectionException;

    void mkdirs(String path, String permission) throws InvalidRequestDataException, UnableToRetrieveResponseException, UnableToSendRequestException, HdfsConnectionException;

    void deleteDirectory(String path) throws InvalidRequestDataException, UnableToRetrieveResponseException, UnableToSendRequestException, HdfsConnectionException;

    void append(String path, int bufferSize, InputStream payload)
            throws InvalidRequestDataException, UnableToRetrieveResponseException, UnableToSendRequestException, HdfsConnectionException;;

    void deleteFile(String path) throws InvalidRequestDataException, UnableToRetrieveResponseException, UnableToSendRequestException, HdfsConnectionException;

    MetaDataDTO getMetadata(String path) throws InvalidRequestDataException, UnableToRetrieveResponseException, UnableToSendRequestException, HdfsConnectionException;

    void rename(String source, String destination) throws InvalidRequestDataException, UnableToRetrieveResponseException, UnableToSendRequestException, HdfsConnectionException;

    void copyToLocalFile(boolean deleteSource, boolean useRawLocalFileSystem, String source, String destination)
            throws InvalidRequestDataException, UnableToRetrieveResponseException, UnableToSendRequestException, HdfsConnectionException;

    void copyFromLocalFile(boolean deleteSource, boolean overwrite, String source, String destination)
            throws InvalidRequestDataException, UnableToRetrieveResponseException, UnableToSendRequestException, HdfsConnectionException;

    void setPermission(String path, String permission) throws InvalidRequestDataException, UnableToRetrieveResponseException, UnableToSendRequestException, HdfsConnectionException;

    void setOwner(String path, String ownername, String groupname)
            throws InvalidRequestDataException, UnableToRetrieveResponseException, UnableToSendRequestException, HdfsConnectionException;

}
