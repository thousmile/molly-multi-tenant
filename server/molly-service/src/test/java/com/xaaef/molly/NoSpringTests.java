package com.xaaef.molly;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.IdUtil;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.xaaef.molly.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.common.consts.JwtConst;
import com.xaaef.molly.common.util.ExcelUtils;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.internal.dto.OperateUserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import liquibase.integration.spring.MultiTenantSpringLiquibase;
import lombok.*;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResourceLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.xaaef.molly.common.util.IpUtils.getLocalIPS;


public class NoSpringTests {

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


    @Test
    public void test8() {
        var port = 19222;
        getLocalIPS().stream()
                .map(ip -> String.format("http://%s:%d/doc.html", ip, port))
                .forEach(System.out::println);
    }


    @Test
    public void test9() {
        System.out.println(JwtSecurityUtils.encryptPassword("test123456"));
        System.out.println(JwtSecurityUtils.encryptPassword("test456789"));
    }


    @Test
    public void test10() throws Exception {
        var ds1 = new MysqlDataSource();
        ds1.setURL("jdbc:mysql://192.168.0.188:3306/hello1?characterEncoding=utf8&useSSL=false");
        ds1.setUser("test123");
        ds1.setPassword("mht123456");
        var liquibase1 = new MultiTenantSpringLiquibase();
        liquibase1.setDataSource(ds1);
        liquibase1.setResourceLoader(new FileSystemResourceLoader());
        liquibase1.setChangeLog("C:\\Users\\demo\\code\\javaProjects\\molly-multi-tenant\\server\\molly-service\\src\\main\\resources\\db\\changelog-other.xml");
        liquibase1.setSchemas(List.of("hello1", "hello2"));
        liquibase1.afterPropertiesSet();
    }


    @Test
    public void test11() throws Exception {
        // 登录接口，也需要，添加租户ID
        var whiteList = Stream.of(JwtConst.WHITE_LIST, JwtConst.TENANT_WHITE_LIST)
                .flatMap(Stream::of)
                .filter(s -> !JwtConst.LOGIN_URL.equals(s))
                .distinct()
                .toList();
        String formatJson = JsonUtils.toFormatJson(Map.of(
                "data", whiteList,
                "time", LocalTime.now()
        ));
        System.out.println(formatJson);
    }

    @Getter
    @Setter
    @Builder
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TestExportEntity implements java.io.Serializable {

        @Schema(description = "ID")
        private Long id;

        @Schema(description = "名称")
        private String name;

        @Schema(description = "爱好")
        protected List<String> hobby;

        @Schema(description = "创建日期")
        protected LocalDate createDate;

        @Schema(description = "创建时间")
        protected LocalTime createTime;

        @Schema(description = "创建日期时间")
        protected LocalDateTime createDateTime;

        @Schema(description = "创建人信息")
        protected OperateUserDTO createUserEntity;

    }


    @Test
    public void test12() throws IOException {
        var treeNodes = List.of(
                new TestExportEntity(1001L, "角色1", List.of("A1", "B1"), LocalDate.now(), LocalTime.now(), LocalDateTime.now(), new OperateUserDTO(1L, "a", "张三")),
                new TestExportEntity(1002L, "角色2", List.of("A2", "B2"), LocalDate.now(), LocalTime.now(), LocalDateTime.now(), new OperateUserDTO(2L, "b", "李四")),
                new TestExportEntity(1003L, "角色3", List.of("A3", "B3"), LocalDate.now(), LocalTime.now(), LocalDateTime.now(), new OperateUserDTO(3L, "c", "王五")),
                new TestExportEntity(1004L, "角色4", List.of("A4", "B4"), LocalDate.now(), LocalTime.now(), LocalDateTime.now(), new OperateUserDTO(4L, "d", "陈六")),
                new TestExportEntity(1005L, "角色5", List.of("A5", "B5"), LocalDate.now(), LocalTime.now(), LocalDateTime.now(), new OperateUserDTO(5L, "e", "赵七"))
        );
        var entity = ExcelUtils.deviceExport("aa.xlsx", treeNodes);
        var file = Files.createFile(Path.of("aa.xlsx")).toFile();
        FileUtil.writeFromStream(entity.getBody().getInputStream(), file, true);
    }


}
