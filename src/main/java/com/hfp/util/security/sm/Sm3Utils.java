package com.hfp.util.security.sm;
import java.io.UnsupportedEncodingException;
import org.bouncycastle.crypto.digests.SM3Digest;

/**
 * 消息摘要：国密 sm3
 *
 */
public class Sm3Utils {
	//private static Logger logger = Logger.getLogger(Sm3Utils.class);

	public static byte[] sm3(byte[] data) {
		
		byte[] result = null;
		try {
			SM3Digest sm3 = new SM3Digest();
			sm3.update(data, 0, data.length);
			result = new byte[sm3.getDigestSize()];
			sm3.doFinal(result, 0);
		} catch (Exception e) {
			//logger.error("Fail: SM3 byte[] to byte[]", e);
		}
		return result;
	}
	
	/**
	 * sm3计算
	 * 
	 * @param datas
	 *            待计算的数据
	 * @param encoding
	 *            字符集编码
	 * @return
	 */
	private static byte[] sm3(String datas, String encoding) {
		try {
			return sm3(datas.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			//LogUtil.writeErrorLog("SM3计算失败", e);
			return null;
		}
	}
	
	public static void main(String[] args){
		
	}
}
