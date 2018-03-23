package com.ethjava;

import com.ethjava.utils.Environment;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

/**
 * 账号管理相关
 */
public class AccountManager {
	private static Admin admin;
	private static final String password="123456789";

	public static void main(String[] args) {
		admin = Admin.build(new HttpService(Environment.RPC_URL));
		String address=createNewAccount();
		getAccountList();
		unlockAccount(address);

//		admin.personalSendTransaction(); 该方法与web3j.sendTransaction相同 不在此写例子。
	}

	/**
	 * 创建账号
	 * @return 
	 */
	private static String createNewAccount() {
		String address="";
		try {
			NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
			address = newAccountIdentifier.getAccountId();
			System.out.println("new account address " + address);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return address;
	}

	/**
	 * 获取账号列表
	 */
	private static void getAccountList() {
		try {
			PersonalListAccounts personalListAccounts = admin.personalListAccounts().send();
			List<String> addressList;
			addressList = personalListAccounts.getAccountIds();
			System.out.println("account size " + addressList.size());
			for (String address : addressList) {
				System.out.println(address);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 账号解锁
	 * @param address 
	 */
	private static void unlockAccount(String address) {
		//账号解锁持续时间 单位秒 缺省值300秒
		BigInteger unlockDuration = BigInteger.valueOf(60L);
		try {
			PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(address, password, unlockDuration).send();
			Boolean isUnlocked = personalUnlockAccount.accountUnlocked();
			System.out.println("account unlock " + isUnlocked);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
