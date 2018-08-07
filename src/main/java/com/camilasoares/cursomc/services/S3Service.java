package com.camilasoares.cursomc.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class S3Service {



	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3client;

	@Value ("${s3.bucket}")
	private String bucketName;
//
//	public URI uploadFile(MultipartFile multipartFile) throws FilerException {
//		try {
//			String fileName = multipartFile.getOriginalFilename();
//			InputStream is = multipartFile.getInputStream();
//			String contentType = multipartFile.getContentType();
//			return uploadFile(is, fileName, contentType);
//		} catch (IOException e) {
//			throw new FilerException ("Erro de IO: " + e.getMessage());
//		}
//	}
//
//	public URI uploadFile(InputStream is, String fileName, String contentType) throws FilerException {
//		try {
//			ObjectMetadata meta = new ObjectMetadata ();
//			meta.setContentType(contentType);
//			LOG.info("Iniciando upload");
//			s3client.putObject(bucketName, fileName, is, meta);
//			LOG.info("Upload finalizado");
//			return s3client.getUrl(bucketName, fileName).toURI();
//		} catch (URISyntaxException e) {
//			throw new FilerException ("Erro ao converter URL para URI");
//		}
//	}

	public void uploadFile(String localFilePath){
		try {
			File file = new File ( localFilePath );
			LOG.info ( "Iniciando upload" );
			s3client.putObject ( new PutObjectRequest ( bucketName , "teste.jpg" , file ) );
			LOG.info ( "Upload finalizado." );
		}catch (AmazonServiceException e){
			LOG.info ( "AmazonServiceException:" + e.getErrorMessage () );
			LOG.info ( "Status Code:" + e.getErrorCode () );
		}
		catch (AmazonClientException e){
			LOG.info ( "AmazonClientException:" + e.getMessage () );
		}
	}
}


