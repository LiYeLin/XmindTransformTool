package com.sankuai.meituan.xmind;

import org.apache.commons.lang3.StringUtils;
import org.xmind.core.*;
import org.xmind.core.marker.IMarkerRef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by guokaiqiang on 2018/5/23.
 */
public class XmindUtil {

    public static List<TestCase> readXmind(String file) {
        IWorkbookBuilder builder = Core.getWorkbookBuilder();//初始化builder
        IWorkbook workbook = null;
//            workbook = builder.loadFromPath("/Users/guokaiqiang/Documents/业务/调度/分商户压单支持多时段/分商户压单支持多时段-测试用例.xmind");//打开XMind文件
        try {
            workbook = builder.loadFromPath(file);//打开XMind文件
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        }
        ISheet defSheet = workbook.getPrimarySheet();//获取主Sheet
        ITopic rootTopic = defSheet.getRootTopic(); //获取根Topic
        List<TestCase> caseList = new ArrayList<TestCase>();
        List directorys = new ArrayList<String>();
        readTopic(rootTopic, caseList, directorys);
//        for (TestCase testCase : caseList) {
//            System.out.println(testCase.toString());
//        }
        return caseList;
    }

    private static void readTopic(ITopic rootTopic, List<TestCase> caseList, List<String> directorys) {
        if (rootTopic.getAllChildren().size() > 0) {
            for (ITopic topic : rootTopic.getAllChildren()) {
                if (topic.getAllChildren().size() > 0) {
                    directorys.add(topic.getTitleText());
                    readTopic(topic, caseList, directorys);
                    if (directorys.size() >= 1){
                        directorys.remove(directorys.size() - 1);
                    }
                } else {
                    TestCase testCase = new TestCase();
                    List<String> tempDirectory = new ArrayList<>();
                    tempDirectory.addAll(directorys);
                    testCase.setDirectory(tempDirectory);
                    testCase.setName(topic.getTitleText());
                    testCase.setLevel(getCaseLevel(topic));
                    testCase.setAutomationCoveredOrRegressionCase(getCaseAutomationCovered(topic));
                    setCaseDetail(topic, testCase);
                    caseList.add(testCase);
                }
            }
        } else {
            System.out.println(rootTopic.getTitleText());
        }
    }

    /**
     * 给文件夹赋值？
     * @param directorys
     * @param Separator
     * @return
     */
    public static String ConvertToDirectory(List<String> directorys, String Separator) {
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < directorys.size() - 1; i++) {
            res.append(directorys.get(i)).append(Separator);
        }
        res.append(directorys.get(directorys.size() - 1));
        return res.toString();
    }

    /**
     * 获取用例级别
     * @param topic
     * @return
     */
    private static String getCaseLevel(ITopic topic) {
        Set<IMarkerRef> markerRefSet = topic.getMarkerRefs();
        for (IMarkerRef iMarkerRef : markerRefSet) {
            if (iMarkerRef.getMarkerId().equals("priority-1")) {
                return "高";
            } else if (iMarkerRef.getMarkerId().equals("priority-2")) {
                return "中";
            } else if (iMarkerRef.getMarkerId().equals("priority-3")) {
                return "低";
            } else {
                continue;
            }
        }
        return "中";
    }

    /**
     * ???
     * @param topic
     * @return
     */
    private static String getCaseAutomationCovered(ITopic topic) {
        Set<IMarkerRef> markerRefSet = topic.getMarkerRefs();
        for (IMarkerRef iMarkerRef : markerRefSet) {
            if (iMarkerRef.getMarkerId().equals("star-green")) {
                return "自动化已覆盖|";
            } else {
                continue;
            }
        }
        return "";
    }

    /**
     * 目测是一个已经放弃了的方法
     * set用例详细信息
     * @param topic 标题
     * @param testCase 用例
     */
    private static void setCaseDetailOld(ITopic topic, TestCase testCase) {
        INotes iNotes = topic.getNotes();
        if (!iNotes.isEmpty()) {
            IPlainNotesContent iPlainNotesContent = (IPlainNotesContent) iNotes.getContent(INotes.PLAIN);
            String comment = iPlainNotesContent.getTextContent();
            String prePrefix = "前置条件：";
            String stepPrefix = "用例步骤：";
            String expectPrefix = "预期结果：";
            int preIndex = comment.indexOf(prePrefix);
            int stepIndex = comment.indexOf(stepPrefix);
            int expectIndex = comment.indexOf(expectPrefix);
            if (preIndex >= 0 && stepIndex >= 0 && expectIndex >= 0) {
                testCase.setPrecondition(comment.substring(preIndex, stepIndex).substring(prePrefix.length()));
                testCase.setStep(comment.substring(stepIndex, expectIndex).substring(stepPrefix.length()));
                testCase.setExpect(comment.substring(expectIndex).substring(expectPrefix.length()));
            } else if (preIndex >= 0 && stepIndex >= 0 && expectIndex < 0) {
                testCase.setPrecondition(comment.substring(preIndex, stepIndex).substring(prePrefix.length()));
                testCase.setStep(comment.substring(stepIndex).substring(stepPrefix.length()));
                testCase.setExpect("");
            } else if (preIndex >= 0 && stepIndex < 0 && expectIndex >= 0) {
                testCase.setPrecondition(comment.substring(preIndex, expectIndex).substring(prePrefix.length()));
                testCase.setStep("");
                testCase.setExpect(comment.substring(expectIndex).substring(expectPrefix.length()));
            } else if (preIndex >= 0 && stepIndex < 0 && expectIndex < 0) {
                testCase.setPrecondition(comment.substring(preIndex).substring(prePrefix.length()));
                testCase.setStep("");
                testCase.setExpect("");
            } else if (preIndex < 0 && stepIndex >= 0 && expectIndex >= 0) {
                testCase.setPrecondition("");
                testCase.setStep(comment.substring(stepIndex, expectIndex).substring(stepPrefix.length()));
                testCase.setExpect(comment.substring(expectIndex).substring(expectPrefix.length()));
            } else if (preIndex < 0 && stepIndex >= 0 && expectIndex < 0) {
                testCase.setPrecondition("");
                testCase.setStep(comment.substring(stepIndex).substring(stepPrefix.length()));
                testCase.setExpect("");
            } else if (preIndex < 0 && stepIndex < 0 && expectIndex >= 0) {
                testCase.setPrecondition("");
                testCase.setStep("");
                testCase.setExpect(comment.substring(expectIndex).substring(expectPrefix.length()));
            } else if (preIndex < 0 && stepIndex < 0 && expectIndex < 0) {
                testCase.setPrecondition("");
                testCase.setStep(comment);
                testCase.setExpect("");
            }
        }
    }

    private static void setCaseDetail(ITopic topic, TestCase testCase) {
        INotes iNotes = topic.getNotes();
        if (!iNotes.isEmpty()) {
            IPlainNotesContent iPlainNotesContent = (IPlainNotesContent) iNotes.getContent(INotes.PLAIN);
            String comment = iPlainNotesContent.getTextContent();
            testCase.setRequirementId(getValueByKey(comment, "需求ID"));
            testCase.setPrecondition(getValueByKey(comment, "前置条件"));
            testCase.setStep(getValueByKey(comment, "用例步骤"));
            testCase.setExpect(getValueByKey(comment, "预期结果"));
            testCase.setType(getValueByKey(comment, "用例类型"));
            testCase.setStatus(getValueByKey(comment, "用例状态"));
            testCase.setCreater(getValueByKey(comment, "创建人"));
            testCase.setSubproject(getValueByKey(comment, "子项目"));
            testCase.setVersion(getValueByKey(comment, "版本信息"));
            testCase.setSpecialDeliverySchedulingStrategy(getValueByKey(comment, "专送调度策略"));
            testCase.setServicePacksOrOrderSources(getValueByKey(comment, "服务包&订单来源"));
            testCase.setCrowdsourcingSchedulingStrategy(getValueByKey(comment, "众包调度策略"));
            testCase.setPmSourceRequirements(getValueByKey(comment, "PM来源需求"));
            testCase.setRdSourceRequirements(getValueByKey(comment, "RD来源需求"));
            testCase.setBusinessModule(getValueByKey(comment, "业务模块"));
            String regressionCase = getValueByKey(comment, "回归用例");
            if (StringUtils.isNotEmpty(regressionCase)){
                regressionCase = regressionCase + "|";
            }
            testCase.setAutomationCoveredOrRegressionCase(testCase.getAutomationCoveredOrRegressionCase() + regressionCase);
            testCase.setDescription(getValueByKey(comment, "用例描述"));
        }
    }

    private static String replaceStartAndEndBlank(String str) {
        Pattern pt = Pattern.compile("^\\s*|\\s*$");
        Matcher mt = pt.matcher(str);
        str = mt.replaceAll("");
        return str;
    }

    private static String getValueByKey(String str, String key){
        String value = StringUtils.substringBetween(str, "【" + key + "】", "【");
        if (StringUtils.isEmpty(value)){
            value = StringUtils.substringAfter(str, "【" + key + "】");
        }
        return replaceStartAndEndBlank(value);
    }

/*    public static void main(String[] args) {
        String a = "【自动化已覆盖&回归用例】    \n" +
                "1.存在可以使用的商家ID\n" +
                "1.登录烽火台，调度中心》众包分商户压单配置，新建压单规则     \n" +
                "【自动化已覆盖&回归用例】\n" +
                "1.增加了早餐、夜宵、全天的选项";
//        String b = StringUtils.substringBetween(a, "【前置条件】", "【");
//        b = StringUtils.removeFirst(b, "\\s*|\t|\r|\n");
//        b = StringUtils.removePattern(b, "\n");
//        b = StringUtils.removeEnd(b, "\\s*");
//        b = StringUtils.removeEnd(b, "\n");
//        b = StringUtils.removeEnd(b, "\n");
//        System.out.println(b);
//        b = replaceBlank(b);
//        System.out.println(b);
        System.out.println(getValueByKey(a, "自动化已覆盖&回归用例"));
    }*/
}
