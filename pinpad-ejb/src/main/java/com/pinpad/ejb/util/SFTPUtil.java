/**
 * 
 */
package com.pinpad.ejb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import com.pinpad.ejb.exceptions.BOException;
import com.pinpad.ejb.exceptions.BOExceptionUpt;

/**
 * @author H P
 *
 */
@Stateless
public class SFTPUtil {

	private static final Logger logger = Logger.getLogger(SFTPUtil.class.getName());

	/**
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 * @throws JSchException
	 */
	public static Session getSession(String host, int port, String username, String password)
			throws JSchException, BOExceptionUpt {

		JSch.setLogger(new MyLogger());

		JSch nJSch = new JSch();

		Session nSShSession = null;

		Properties config = new Properties();

		nSShSession = nJSch.getSession(username, host, port);
		nSShSession.setPassword(password);

		logger.info("Sesión creada con éxito");

		config.put("compression.s2c", "zlib,none");
		config.put("compression.c2s", "zlib,none");
		config.put("StrictHostKeyChecking", "no");
		nSShSession.setConfig(config);

		nSShSession.connect();
		logger.info("La sesión está conectada");
		return nSShSession;
	}

	/**
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 * @throws JSchException
	 */
	public static ChannelSftp connect(Session nSShSession) throws JSchException {
		// 1. Declarar el canal para conectarse a Sftp
		ChannelSftp nChannelSftp = null;
		// 9. Abre el canal sftp
		Channel channel = nSShSession.openChannel("sftp");
		// 10. Iniciar conexión
		channel.connect();
		nChannelSftp = (ChannelSftp) channel;
		return nChannelSftp;
	}

	/**
	 * Cambiar nombre de archivo
	 * 
	 * @param directory
	 * @param oldname
	 * @param newname
	 * @param sftp
	 */
	public void renameFile(String directory, String oldname, String newname, ChannelSftp sftp) throws BOException {
		try {
			sftp.cd(directory);
			sftp.rename(oldname, newname);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception: " + e.getMessage());
			throw new BOException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * subir archivos
	 * 
	 * @param directory
	 * @param uploadFile
	 * @param sftp
	 */
	public void upload(String directory, Path file, String strFileName, ChannelSftp sftp) throws BOExceptionUpt {
		try {
			sftp.cd(directory);
			// File file = new File(uploadFile);
			sftp.put(new FileInputStream(file.toFile()), strFileName);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception: " + e.getMessage());
			throw new BOExceptionUpt(e.getMessage(), e.getCause());
		}
		sftp.disconnect();
		sftp.exit();
	}

	/**
	 * descargar archivo
	 * 
	 * @param directory
	 * @param downloadFile
	 * @param saveFile
	 * @param sftp
	 */
	public void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) throws BOException {
		try {
			sftp.cd(directory);
			File file = new File(saveFile);
			sftp.get(downloadFile, new FileOutputStream(file));

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception: " + e.getMessage());
			throw new BOException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Borrar archivos
	 * 
	 * @param directory
	 * @param deleteFile
	 * @param sftp
	 */
	public void delete(String directory, String deleteFile, ChannelSftp sftp) throws BOException {
		try {
			sftp.cd(directory);
			sftp.rm(deleteFile);
			logger.info("Eliminado correctamente");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception: " + e.getMessage());
			throw new BOException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Lista los archivos debajo de la lista
	 * 
	 * @param directory
	 * @param sftp
	 * @return
	 * @throws SftpException
	 */
//	public Vector listFiles(String directory, ChannelSftp sftp) throws SftpException {
//		return sftp.ls(directory);
//	}

	/**
	 * Descargue todos los archivos en la carpeta
	 * 
	 * @param viDirectory
	 * @param viHost
	 * @param viPort
	 * @param viUserName
	 * @param viPassWord
	 * @param viSaveDir
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<String> downloadDirFile(String viDirectory, String viHost, int viPort, String viUserName,
			String viPassWord, String viSaveDir) {
		ChannelSftp nChannelSftp = null;
		List<String> nFileNameList = null;
		try {
			Session session = getSession(viHost, 22, viUserName, viPassWord);
			nChannelSftp = connect(session);

			Vector nVector = nChannelSftp.ls(viDirectory);

			for (int i = 0; i < nVector.size(); i++) {

				nChannelSftp.cd(viDirectory);

				String nFileName = nVector.get(i).toString().substring(56, nVector.get(i).toString().length());
				if (!nFileName.contains("csv")) {
					continue;
				}
				File nFile = new File(viSaveDir + File.separator + nFileName);

				nChannelSftp.get(nFileName, new FileOutputStream(nFile));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			nChannelSftp.disconnect();
		}
		return nFileNameList;
	}

	/**
	 * Cerrar servidor de conexión
	 */
	public void logout(ChannelSftp sftp, Session session) {
		if (sftp != null) {
			if (sftp.isConnected()) {
				sftp.disconnect();
				logger.info("sftp is closed already");
			}
		}
		if (session != null) {
			if (session.isConnected()) {
				session.disconnect();
			}
		}
	}

	public static void uploadFile(Path file, String strFileName, String strRutaSftp, String strIpServer,
			Integer intPuerto, String strUser, String strPassword) throws JSchException, BOExceptionUpt {
		SFTPUtil sf = new SFTPUtil();
		Session session = getSession(strIpServer, intPuerto, strUser, strPassword);
		ChannelSftp cs = connect(session);
		sf.upload(strRutaSftp, file, strFileName, cs);
		sf.logout(cs, session);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static class MyLogger implements com.jcraft.jsch.Logger {
		static java.util.Hashtable name = new java.util.Hashtable();
		static {
			name.put(new Integer(DEBUG), "DEBUG: ");
			name.put(new Integer(INFO), "INFO: ");
			name.put(new Integer(WARN), "WARN: ");
			name.put(new Integer(ERROR), "ERROR: ");
			name.put(new Integer(FATAL), "FATAL: ");
		}

		public boolean isEnabled(int level) {
			return true;
		}

		public void log(int level, String message) {
			logger.info((String) name.get(new Integer(level)));
			logger.info(message);
		}
	}

	public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {
		@Override
		public String getPassphrase() {
			return null;
		}

		@Override
		public String getPassword() {
			return null;
		}

		@Override
		public boolean promptPassphrase(String arg0) {
			return false;
		}

		@Override
		public boolean promptPassword(String arg0) {
			return false;
		}

		@Override
		public boolean promptYesNo(String arg0) {
			return false;
		}

		@Override
		public void showMessage(String arg0) {
		}

		@Override
		public String[] promptKeyboardInteractive(String arg0, String arg1, String arg2, String[] arg3,
				boolean[] arg4) {
			return null;
		}
	}

}