package com.xaaef.molly;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.IdUtil;
import com.xaaef.molly.common.util.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;


class NoSpringTests {

    @Test
    public void test1() {
        boolean after = LocalDateTime.now().isAfter(LocalDateTime.of(LocalDate.of(2023, 12, 28), LocalTime.MAX));
        System.out.println(after);
    }


    @Test
    public void test3() {
        System.out.println(FileUtil.extName("https://images.xaaef.com/base/63b7df8b9e8581f5bff107e2.jpg"));
    }


    @Test
    public void test4() {
        boolean isMatch = Pattern.matches("\\w{4,12}$", "你好啊啊啊dwa541");
        System.out.println(isMatch);
    }


    @Test
    public void test5() {
        byte[] bytes = IdUtil.getSnowflakeNextIdStr().getBytes();
        System.out.println(new String(bytes));
        System.out.println(bytes.length);
    }


    @Test
    public void test6() {
        var treeNodes = List.of(
                new TreeNode<>(1001, 10, "部门1", 1),
                new TreeNode<>(1002, 11, "部门2", 3),
                new TreeNode<>(1003, 1002, "部门2", 4),
                new TreeNode<>(1004, 1002, "部门2", 5),
                new TreeNode<>(1005, 1001, "部门2", 6)
        );
        var first = treeNodes.stream().map(TreeNode::getParentId).sorted().findFirst().get();
        List<Tree<Integer>> build = TreeUtil.build(treeNodes, first);
        System.out.println(JsonUtils.toFormatJson(build));
    }


    @Test
    public void test7() {
        var l = Duration.ofMinutes(1).toMillis();
        System.out.println(l);
    }


}
