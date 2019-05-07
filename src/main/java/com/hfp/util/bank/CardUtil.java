package com.hfp.util.bank;

public class CardUtil {
	


	/**
	 * 
	 * @param aPin
	 * @return
	 */
	public static byte[] pin2PinBlock(String aPin) {
		int tTemp = 1;
		int tPinLen = aPin.length();

		byte[] tByte = new byte[8];
		try {
			/*******************************************************************
			 * if (tPinLen > 9) { tByte[0] = (byte) Integer.parseInt(new
			 * Integer(tPinLen) .toString(), 16); } else { tByte[0] = (byte)
			 * Integer.parseInt(new Integer(tPinLen) .toString(), 10); }
			 ******************************************************************/
//			tByte[0] = (byte) Integer.parseInt(new Integer(tPinLen).toString(), 10);
			tByte[0] = (byte) Integer.parseInt(Integer.toString(tPinLen), 10);
			if (tPinLen % 2 == 0) {
				for (int i = 0; i < tPinLen;) {
					String a = aPin.substring(i, i + 2);
					tByte[tTemp] = (byte) Integer.parseInt(a, 16);
					if (i == (tPinLen - 2)) {
						if (tTemp < 7) {
							for (int x = (tTemp + 1); x < 8; x++) {
								tByte[x] = (byte) 0xff;
							}
						}
					}
					tTemp++;
					i = i + 2;
				}
			} else {
				for (int i = 0; i < tPinLen - 1;) {
					String a;
					a = aPin.substring(i, i + 2);
					tByte[tTemp] = (byte) Integer.parseInt(a, 16);
					if (i == (tPinLen - 3)) {
						String b = aPin.substring(tPinLen - 1) + "F";
						tByte[tTemp + 1] = (byte) Integer.parseInt(b, 16);
						if ((tTemp + 1) < 7) {
							for (int x = (tTemp + 2); x < 8; x++) {
								tByte[x] = (byte) 0xff;
							}
						}
					}
					tTemp++;
					i = i + 2;
				}
			}
		} catch (Exception e) {
		}

		return tByte;
	}

	/**
	 * @param aPin  密码
	 * @param aCardNO
	 * @return
	 */
	public static byte[] pin2PinBlockWithCardNO(String aPin, String aCardNO) {
		byte[] tPinByte = pin2PinBlock(aPin);
		if (aCardNO.length() == 11) {
			aCardNO = "00" + aCardNO;
		} else if (aCardNO.length() == 12) {
			aCardNO = "0" + aCardNO;
		}
		byte[] tPanByte = formatPan(aCardNO);
		byte[] tByte = new byte[8];
		for (int i = 0; i < 8; i++) {
			tByte[i] = (byte) (tPinByte[i] ^ tPanByte[i]);
		}
		return tByte;
	}
	


	/**
	 * 
	 * @param aPan
	 * @return
	 */
	private static byte[] formatPan(String aPan) {
		int tPanLen = aPan.length();
		byte[] tByte = new byte[8];
		;
		int temp = tPanLen - 13;
		try {
			tByte[0] = (byte) 0x00;
			tByte[1] = (byte) 0x00;
			for (int i = 2; i < 8; i++) {
				String a = aPan.substring(temp, temp + 2);
				tByte[i] = (byte) Integer.parseInt(a, 16);
				temp = temp + 2;
			}
		} catch (Exception e) {
		}
		return tByte;
	}
	
	/** 
	* 根据Luhn算法生成校验码
	* @param number
	* @return 
	*/ 
	public static int genLuhn(String number) {
		// 卡号字符转换成数字
	    int[] cardNoArr = new int[number.length()];
	    for (int i=0; i<number.length(); i++) {
	    	// Character.digit(number.charAt(i), 10);
	        cardNoArr[i] = Integer.valueOf(String.valueOf(number.charAt(i)));
	    }
	    /*
	    // 按照从右往左的顺序,从这串数字的右边开始,
	    // 将奇数位数字乘以2,如果每次乘二操作的结果大于9(如 8 × 2 = 16),
	    // 然后计算个位和十位数字的和(如 1 + 6 = 7)或者用这个结果减去9(如 16 - 9 = 7);
	       6 2 1 4 8 3 7 5 5 4 4 3 5 1 4  <= 数字
	       12  2   16  14  10  8   10  8
	       3 2 2 4 7 3 5 5 1 4 8 3 1 1 8    sum => 57
	    */
	    for(int i=cardNoArr.length-1; i>=0; i-=2) {
	        cardNoArr[i] <<= 1;
	        cardNoArr[i] = cardNoArr[i]/10 + cardNoArr[i]%10;
	    }
	    
	    //
	    int sum = 0; 
	    for(int i=0;i<cardNoArr.length;i++) { 
	        sum += cardNoArr[i]; 
	    }

	    return (10-(sum%10))%10; 
	}
	
   /**
    * luhn算法生成校验码
    * 
    * @param number
    * @return
    */
   public static int genLuhn1(String number) {
       number = new StringBuffer(number+"0").reverse().toString();
       int sum = 0;
       int tmp;
       for (int i = 0; i < number.length(); i++) {
           int digit = Character.digit(number.charAt(i), 10);
           if (i % 2 == 0) {
               sum += digit;
           } else {
               tmp = 2*digit;
               if (tmp > 9) {
                   sum += (tmp-9);
               }else{
            	   sum+=tmp;
               }
           }
       }

       return (10-(sum%10))%10;
   }
	
	
	

	/** 
	* 匹配Luhn算法:可用于检测银行卡卡号 
	* @param cardNo 
	* @return 
	*/ 
	public static boolean matchLuhn(String cardNo) {	
	    return genLuhn(cardNo.substring(0,cardNo.length()-1)) == Integer.valueOf(String.valueOf(cardNo.charAt(cardNo.length()-1))); 
	}
	
	
   
   public static void main(String[] args){
//	   System.out.println(genLuhn1("621483755443514"));
//	   System.out.println(matchLuhn("6214837554435143"));
//	   System.out.println(matchLuhn("6212261001039208159"));
   }
}
