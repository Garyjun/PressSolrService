package com.channelsoft.appframe.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * <p>Title: 产生随机数的工具类。</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author 郑伟
 * @version 1.0
 */

public class RandomUtils {
	private final Random random;

	private RandomUtils() {
		random = new Random();
	}

	/**
	 * 生成num个长度为length的字符串（字符串各不相同）,字符串只包含字母
	 * @param length 字符串的长度
	 * @param num 字符串的个数
	 * @return
	 */
	public static String[] random(final int length, final int num) {
		return new RandomUtils().buildRandom(length, num);
	}

	/**
	 *  生成长度为length的字符串,字符串只包含数字
	 * @param length 字符串的长度
	 * @return
	 */
	public static String random(final int length) {
		return new RandomUtils().buildRandom(length);
	}

	/**
	 * 生成num个长度为length的字符串（字符串各不相同）,字符串只包含字母
	 * @param length 字符串的长度
	 * @param num 字符串的个数
	 * @return
	 */
	private String[] buildRandom(final int length, final int num) {
		if (num < 1 || length < 1) {
			return null;
		}
		Set<String> tempRets = new HashSet<String>(num); //存放临时结果，以避免重复值的发生

		//生成num个不相同的字符串
		while (tempRets.size() < num) {
			tempRets.add(buildRandom(length));
		}

		String[] rets = new String[num];
		rets = tempRets.toArray(rets);
		return rets;
	}

	/**
	 *  生成长度为length的字符串,字符串只包含数字
	 * @param length 字符串的长度
	 * @return
	 */
	private String buildRandom(final int length) {
		//长度为length的最多整数
		String maxStr = StringUtils.rightPad("1", length + 1, '0');
		//        System.out.println("maxStr=" + maxStr);
		long max = Long.parseLong(maxStr);
		long i = random.nextLong(); //取得随机数
		//        System.out.println("befor i=" + i);
		i = Math.abs(i) % max; //取正数，并限制其长度
		//        System.out.println("after i=" + i);
		String value = StringUtils.leftPad(String.valueOf(i), length, '0');
		//        System.out.println("length=" + length);
		//        System.out.println("value=" + value);
		return value;
	}
}
