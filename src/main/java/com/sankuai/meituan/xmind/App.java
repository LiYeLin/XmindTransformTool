package com.sankuai.meituan.xmind;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guokaiqiang on 2018/5/24.
 */
public class App {
    public static void main(String[] args) {
        final String  LOGO = " ______     __   __     ______     ______    \n" +
                "/\\  __ \\   /\\ \"-.\\ \\   /\\  ___\\   /\\  ___\\   \n" +
                "\\ \\ \\/\\ \\  \\ \\ \\-.  \\  \\ \\  __\\   \\ \\___  \\  \n" +
                " \\ \\_____\\  \\ \\_\\\\\"\\_\\  \\ \\_____\\  \\/\\_____\\ \n" +
                "  \\/_____/   \\/_/ \\/_/   \\/_____/   \\/_____/ \n" +
                "                                             ";
        String mode = "";
        String file = "";
        String rootDir = "";
        boolean tapdFlag = false;
        boolean testlinkFlag = false;
        //ones模式的标志
        boolean onesFlag = true;
        boolean success = false;
        for (int i=0; i<args.length; i++){
            //解析时候根据输入的参数不同 用不同的模式
            if ("-m".equals(args[i])){
                checkLength(i, args.length);
                mode = args[i + 1];
            }
            if ("-f".equals(args[i])){
                checkLength(i, args.length);
                file = args[i + 1];
            }
            if ("-d".equals(args[i])){
                checkLength(i, args.length);
                rootDir = args[i + 1];
            }
            if ("-t".equals(args[i])){
                tapdFlag = true;
            }
            if ("-l".equals(args[i])){
                testlinkFlag = true;
            }
        }
//        if (!((args.length == 2 && args[0].equals("case")) || (args.length >= 3 && (args[0].equals("case") || args[0].equals("dir"))))) {
//            errorMessage();
//        }
        //检查是不是xmind文件
        if (StringUtils.endsWith(file, ".xmind")){
            String filePath = file.substring(0, file.indexOf(".xmind"));
            List<TestCase> caseList = XmindUtil.readXmind(file);
            //检查
            if (tapdFlag && ("case".equals(mode) || "dir".equals(mode))){
                if ("dir".equals(mode) && StringUtils.isBlank(rootDir)){
                    errorMessage();
                }
                System.out.println(LOGO);
                System.out.println("正在疯狂转换tapd所需excel测试用例文件中...");
                String excelFile = filePath + "_tapd.xlsx";
                toTapdExcel(caseList, mode, rootDir, excelFile);
                System.out.println("转换成功 文件为：" + excelFile);
                success = true;
            }
            if (testlinkFlag && StringUtils.isNotBlank(file)){
                System.out.println(LOGO);
                System.out.println("正在疯狂转换testlink所需excel测试用例文件中...");
                String excelFile = filePath + "_testlink.xlsx";
                toTestlinkExcel(caseList, excelFile);
                System.out.println("转换成功 文件为：" + excelFile);
                success = true;
            }
            //onesFlag写死为true
            if (onesFlag){
                System.out.println(LOGO);
                System.out.println("-------------------正在疯狂转换ones所需excel测试用例文件中-------------------");
                String excelFile = filePath + "_ones.xlsx";
                toOnesExcel(caseList, mode, rootDir, excelFile);
                System.out.println("转换成功! 文件为：" + excelFile);
                success = true;
            }
        }
        if (!success){
            errorMessage();
        }
    }

    public static void errorMessage() {
        System.out.print("参数填写错误！！！\n" +
                "参数列表：\n" +
                "   -f，xmind文件（必填项）\n" +
                "   -t，生产tapd所需文件（当选择-l时，选填）\n" +
                "   -l，生产testlink所需文件（当选择-t时，选填）\n" +
                "   -m，模式（选填，当选择-t时必填）\n" +
                "       dir模式: 在tapd根路径导入，生成目录结构\n" +
                "       case模式: 用例标题即为xmind主题顺序，可在tapd指定目录导入，不自动生成目录\n" +
                "       层级较少时，推荐使用dir模式，层级较深时，推荐使用case模式\n" +
                "   -d，目录（选填，当选择dir模式时必填）\n" +
                "       在tapd中完整用例目录，用“-”分隔\n" +
                "参考例子:\n" +
                "   java -jar xmind-case-to-excel-1.3.jar -f ./xxx.xmind -m dir -t -d 根目录1-目录2\n" +
                "   java -jar xmind-case-to-excel-1.3.jar -f /xxx/xxx.xmind -m case -t\n" +
                "   java -jar xmind-case-to-excel-1.3.jar -f xxx.xmind -l\n" +
                "提醒：\n" +
                "   1. Excel文件会生成在和XMind文件同级目录下\n" +
                "   2. -t和-l参数至少选择一个，可以同时选择生成\n"
        );
        System.out.println("--------------------------------------------------");
        System.out.println("XMind测试用例转Excel测试用例工具");
        System.out.println("--------------------------------------------------");
        return;
    }

    private static void toTapdExcel(List<TestCase> caseList, String mode, String rootDir, String excelFile){
        List<String> headers = new ArrayList<>();
        headers.add("用例目录");
        headers.add("用例名称");
        headers.add("需求ID");
        headers.add("前置条件");
        headers.add("用例步骤");
        headers.add("预期结果");
        headers.add("用例类型");
        headers.add("用例状态");
        headers.add("用例等级");
        headers.add("创建人");
        headers.add("子项目");
        headers.add("版本信息");
        headers.add("专送调度策略");
        headers.add("业务类型");
        headers.add("服务包&订单来源");
        headers.add("众包调度策略");
        headers.add("PM来源需求");
        headers.add("RD来源需求");
        headers.add("业务模块");
        headers.add("自动化已覆盖&回归用例");

        List list = new ArrayList();
        for (TestCase testCase : caseList) {
            List<String> tempList = new ArrayList<>();
            if (mode.equals("dir")) {
                tempList.add(rootDir + " - " + XmindUtil.ConvertToDirectory(testCase.getDirectory(), " - "));
                tempList.add(testCase.getName());
            } else {
                tempList.add("");
                List<String> tempCaseName = testCase.getDirectory();
                tempCaseName.add(testCase.getName());
                tempList.add(XmindUtil.ConvertToDirectory(tempCaseName, "->"));
            }
            tempList.add(testCase.getRequirementId());
            tempList.add(testCase.getPrecondition());
            tempList.add(testCase.getStep());
            tempList.add(testCase.getExpect());
            tempList.add(testCase.getType());
            tempList.add(testCase.getStatus());
            tempList.add(testCase.getLevel());
            tempList.add(testCase.getCreater());
            tempList.add(testCase.getSubproject());
            tempList.add(testCase.getVersion());
            tempList.add(testCase.getSpecialDeliverySchedulingStrategy());
            tempList.add(testCase.getBusinessType());
            tempList.add(testCase.getServicePacksOrOrderSources());
            tempList.add(testCase.getCrowdsourcingSchedulingStrategy());
            tempList.add(testCase.getPmSourceRequirements());
            tempList.add(testCase.getRdSourceRequirements());
            tempList.add(testCase.getBusinessModule());
            tempList.add(testCase.getAutomationCoveredOrRegressionCase());
            list.add(tempList);
        }
        ExcelFileUtil.writeExcelFile(new File(excelFile), headers, list);
    }

    /**
     * 转为ones的Excel
     * @param caseList 用力列表
     * @param mode 模式
     * @param rootDir 根目录
     * @param excelFile 生成的表格文件名？
     */
    private static void toOnesExcel(List<TestCase> caseList, String mode, String rootDir, String excelFile){
        List<String> headers = new ArrayList<>();
        headers.add("目录");
        headers.add("用例标题*");
        headers.add("优先级");
        headers.add("类型");
        headers.add("状态");
        headers.add("创建人");
        headers.add("前置条件");
        headers.add("用例步骤");
        headers.add("预期结果");
        List list = new ArrayList();
        //逐行写入
        for (TestCase testCase : caseList) {
            List<String> tempList = new ArrayList<>();
            if (mode.equals("dir")) {
                tempList.add(rootDir + " - " + XmindUtil.ConvertToDirectory(testCase.getDirectory(), " - "));
                tempList.add(testCase.getName());
            } else {
                List<String> tempCaseName = testCase.getDirectory();
                tempCaseName.add(testCase.getName());
                tempList.add(XmindUtil.ConvertToDirectory(tempCaseName, "/"));
                tempList.add(XmindUtil.convertToTitle(tempCaseName,"TC："));
            }
            //
            //
            tempList.add(testCase.getLevel());
//            暂时写死
            tempList.add("自动化测试");
//            tempList.add(testCase.getType());
            tempList.add(testCase.getStatus());
            tempList.add(testCase.getCreater());
            tempList.add(testCase.getPrecondition());
            tempList.add(testCase.getStep());
            tempList.add(testCase.getExpect());


            list.add(tempList);
        }
        ExcelFileUtil.writeExcelFile(new File(excelFile), headers, list);
    }

    private static void toTestlinkExcel(List<TestCase> caseList, String excelFile){
        //excel 标题
        List<String> headers = new ArrayList<>();
        headers.add("测试用例名称");
        headers.add("用例描述（可填接口文档/需求文档地址）");
        headers.add("前提（测试数据准备）");
        headers.add("步骤编号（数字）");
        headers.add("步骤内容");
        headers.add("期望结果");
        List list = new ArrayList();
        for (TestCase testCase : caseList) {
            List<String> tempList = new ArrayList<>();
            List<String> tempCaseName = testCase.getDirectory();
            tempCaseName.add(testCase.getName());
            tempList.add(XmindUtil.ConvertToDirectory(tempCaseName, "->"));
            tempList.add(testCase.getDescription());
            tempList.add(testCase.getPrecondition());
            tempList.add("1");
            tempList.add(testCase.getStep());
            tempList.add(testCase.getExpect());
            list.add(tempList);
        }
        //testlink模式写入excel
        ExcelFileUtil.writeExcelFile(new File(excelFile), headers, list);
    }

    // TODO: 2020/4/5 检查输入的指令中 ，参数指令是不是在最后？？？
    private static void checkLength(int i, int length){
        if (i + 1 >= length){
            errorMessage();
        }
    }
}
