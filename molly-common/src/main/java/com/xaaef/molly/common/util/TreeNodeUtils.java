package com.xaaef.molly.common.util;

import com.xaaef.molly.common.domain.TreeNode;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 将 List 数据递归成，树节点的形式
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0
 * @date 2021/7/5 9:31
 */


@Slf4j
public class TreeNodeUtils {

    private final static Long ROOT = 0L;

    /**
     * 查找所有根节点
     *
     * @param allNodes 节点
     * @return Set<TreeNode>
     */
    public static <T extends TreeNode<T>> List<T> findRoots(List<T> allNodes) {
        return allNodes.stream()
                .filter(node -> node.getParentId().equals(ROOT))
                .map(node -> findChildren(node, allNodes))
                .sorted(TreeNodeUtils::comparator)
                .collect(Collectors.toList());
    }


    /**
     * 递归查找子节点
     *
     * @param treeNode
     * @param treeNodes
     * @return TreeNode
     */
    private static <T extends TreeNode<T>> T findChildren(T treeNode, List<T> treeNodes) {
        for (T it : treeNodes) {
            if (treeNode.getId().equals(it.getParentId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<T>());
                }
                treeNode.getChildren().add(findChildren(it, treeNodes));
            }
        }
        // 排序
        var children = treeNode.getChildren();
        if (children != null && children.size() > 1) {
            var collect = children.stream()
                    .sorted(TreeNodeUtils::comparator)
                    .distinct()
                    .collect(Collectors.toList());
            treeNode.setChildren(collect);
        }
        return treeNode;
    }


    /**
     * 排序
     *
     * @return TreeNode
     */
    private static <T extends TreeNode<T>> int comparator(TreeNode<T> o1, TreeNode<T> o2) {
        long temp = o1.getSort() - o2.getSort();
        if (temp > 0)
            return 1;
        else if (temp < 0)
            return -1;
        else
            return 0;
    }

}
