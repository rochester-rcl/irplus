package edu.ur.ir.repository.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import edu.ur.file.db.FileInfoChecksumService;
import edu.ur.ir.repository.ChecksumCheckerReportService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;

/**
 * @author Nathan Sarr
 *
 */
public class DefaultChecksumCheckerReportService implements ChecksumCheckerReportService {
	
	/* Java mail sender to send emails */
    private JavaMailSender mailSender;
    
	/*  Get the logger for this class */
	private static final Logger log = LogManager.getLogger(DefaultChecksumCheckerReportService.class);

	/* checksum service */
	private FileInfoChecksumService fileInfoChecksumService;
    
	/* format used for formatting dates */
	private String dateFormat = "MM/dd/yyyy";
	
	/* address the email should be from */
	private String fromAddress = "";
	
	/* Base path for the web app  */
	private String baseWebAppPath;
	
	/* email to send the report to */
	private String email;
	


	/* service to get repository information */
	private RepositoryService repositoryService;
	

	/**
	 * Send an email for reporting what the checksum checker has found.
	 * 
	 * @param repository - repository this checksum checker is running for
	 * @param email - email to send the report to.
	 * 
	 * @throws MessagingException - if a messaging exception occurs
	 */
	public void sendChecksumReportEmail() throws MessagingException 
	{	
		log.debug("Send checksum checker report");
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);

		if( repository != null )
	    {
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		    StringBuffer emailText = new StringBuffer();
		    String reportDateStr = simpleDateFormat.format(new Date());
		
		    emailText.append("Checksum report for " + repository.getName() +  " as of " + reportDateStr + "\n\n");
		    emailText.append("There are: "  + fileInfoChecksumService.getChecksumInfoFailsCount() + " checksum failures in the repository \n");
		    emailText.append("Use this link to review the checksums " + baseWebAppPath + "admin/viewFileInfoChecksums.action \n");
		
		    MimeMessage message = mailSender.createMimeMessage();
		    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, false, "UTF-8");
		    String subject = "Checksum report for " + repository.getName() + " as of: " + reportDateStr;
		    mimeMessageHelper.setSubject(subject);
		    mimeMessageHelper.setFrom(fromAddress);
		    mimeMessageHelper.setTo(email);
		    mimeMessageHelper.setText(emailText.toString());
		    mailSender.send(message);
	    }
		else
		{
			log.debug("No repository found");
		}
	}
	
	public void setFileInfoChecksumService(
			FileInfoChecksumService fileInfoChecksumService) {
		this.fileInfoChecksumService = fileInfoChecksumService;
	}
	
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public void setBaseWebAppPath(String baseWebAppPath) {
		this.baseWebAppPath = baseWebAppPath;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
