package com.self.deploy.web.util;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 用java代码调用git命令的工具包
 *
 * @author shaojieyue
 * @date 2013-07-30 16:29:04
 */
public class GitUtil {
    /**
     * 前缀分隔符
     */
    private static final String PREFX_SPLIT_CHAR = "_";
    private static final String VERSION_SPLIT_CHAR = ".";
    private static final Logger logger = LoggerFactory.getLogger(GitUtil.class);
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 列出project的所有分支
     *
     * @param projectHome project 的目录
     * @return
     * @throws IOException
     * @throws GitAPIException
     */
    public static String[] listBranch(String projectHome) throws IOException, GitAPIException {
        File root = new File(projectHome);
        Git git = Git.open(root);
        List<Ref> list = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
        if (list == null) {
            return new String[0];
        }
        List<String> branchs = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            String name = list.get(i).getName();
            name = name.substring(name.lastIndexOf("/")+1);
            if (name.equalsIgnoreCase("head")) {
                continue;
            }
            branchs.add(name);
        }
        return branchs.stream().toArray(String[]::new);
    }

    /**
     * 根据当前tag号,生成新的tag号,注意:tag号的格式为%_数字.数字.数字...
     *
     * @param currentTag
     * @return
     * @throws IOException
     * @throws GitAPIException
     */
    public static String autoGenerateTag(String currentTag) throws IOException, GitAPIException {
        String[] arr = currentTag.split(PREFX_SPLIT_CHAR);
        if (arr.length < 2) {
            throw new IllegalGitTagPatternException("illegal git tag " + currentTag);
        }

        String mathVersion = arr[arr.length - 1];
        String[] arr1 = mathVersion.split("\\" + VERSION_SPLIT_CHAR);
        int[] lens = new int[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            //分隔符间版本位数
            lens[i] = arr1[i].length();
        }
        //将数字版本号间的分隔符去掉,例如1.1.1取出后为111
        String oldVerStr = mathVersion.replaceAll("\\" + VERSION_SPLIT_CHAR, "");
        long verMath = 0;
        try {
            //版本号累加1
            verMath = Long.valueOf(oldVerStr) + 1;
            if (verMath % 10 == 0) {//末位为0,则继续累加1
                verMath++;
            }
        } catch (Exception e) {
            throw new IllegalGitTagPatternException("illegal git tag " + currentTag);
        }

        if (oldVerStr.length() != (verMath + "").length()) {//超过最大限定抛出异常如:t_9.9,累加后为t_10.1
            throw new GitTagOutOfMaxValueException("old tag math :" + oldVerStr + " new tag math:" + verMath);
        }

        StringBuilder newTag = new StringBuilder();

        for (int i = 0; i < arr.length - 1; i++) {//字符前缀组装
            newTag.append(arr[i]);
            newTag.append(PREFX_SPLIT_CHAR);
        }

        String newVerStr = verMath + "";
        String tmp = null;
        int beginIndex = 0;
        for (int i = 0; i < lens.length; i++) {//数字版本组装
            tmp = newVerStr.substring(beginIndex, beginIndex + lens[i]);
            beginIndex = beginIndex + lens[i];
            newTag.append(tmp);
            newTag.append(VERSION_SPLIT_CHAR);
        }

        return newTag.substring(0, newTag.length() - 1);


    }

}

class IllegalGitTagPatternException extends RuntimeException {

    public IllegalGitTagPatternException(String msg) {
        super(msg);
    }

}

class GitTagOutOfMaxValueException extends RuntimeException {

    public GitTagOutOfMaxValueException(String msg) {
        super(msg);
    }

}
