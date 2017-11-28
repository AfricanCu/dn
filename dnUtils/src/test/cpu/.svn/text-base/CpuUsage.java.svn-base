package test.cpu;

/**
 * @author wanlh
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CpuUsage {
	public double getCpuUsage() throws Exception {
		double cpuUsed = 0;
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec("top -b -n 1");// 调用系统的“top"命令
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String str = null;
			String[] strArray = null;
			while ((str = in.readLine()) != null) {
				int m = 0;
				if (str.indexOf(" R ") != -1 && str.indexOf("top") == -1) {// 只分析正在运行的进程，top进程本身除外
					strArray = str.split(" ");
					for (String tmp : strArray) {
						if (tmp.trim().length() == 0)
							continue;
						if (++m == 9) {// 第9列为CPU的使用百分比(RedHat 9)
							cpuUsed += Double.parseDouble(tmp);
						}
					}
					// System.out.println(str);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return cpuUsed;
	}

	public static void main(String[] args) throws Exception {
		CpuUsage cpu = new CpuUsage();
		System.out.println("cpu used:" + cpu.getCpuUsage() + "%");
	}
}