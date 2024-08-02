package com.xaaef.molly.common.util;

import cn.hutool.core.net.NetUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import static cn.hutool.core.net.NetUtil.LOCAL_IP;

/**
 * <p>
 * </p>
 *
 * @author Wang Chen Chen
 * @version 1.0.1
 * @date 2021/8/3 9:13
 */

@Slf4j
public class IpUtils {

    private static final String IP_UTILS_FLAG = ",";

    private static final String UNKNOWN = "unknown";

    private static final String LOCALHOST_IP = "0:0:0:0:0:0:0:1";

    private static Searcher SEARCHER = null;

    static {
        final var cpr = new ClassPathResource("ip2region.xdb");
        try {
            byte[] dbBinStr = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            SEARCHER = Searcher.newWithBuffer(dbBinStr);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取 IP真实地址
     */
    public static String getRealAddressByIP(String ip) {
        if (NetUtil.isInnerIP(ip)) {
            return "内网IP";
        }
        try {
            return SEARCHER.search(ip);
        } catch (Exception e) {
            return "未知";
        }
    }

    /**
     * 获取IP地址
     * <p>
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        //以下两个获取在k8s中，将真实的客户端IP，放到了x-Original-Forwarded-For。而将WAF的回源地址放到了 x-Forwarded-For了。
        ip = request.getHeader("X-Original-Forwarded-For");
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        //获取nginx等代理的ip
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        //兼容k8s集群获取ip
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (LOCAL_IP.equalsIgnoreCase(ip) || LOCALHOST_IP.equalsIgnoreCase(ip)) {
                //根据网卡取本机配置的IP
                ip = NetUtil.getLocalhostStr();
            }
        }
        //使用代理，则获取第一个IP地址
        if (!StringUtils.isEmpty(ip) && ip.indexOf(IP_UTILS_FLAG) > 0) {
            ip = ip.substring(0, ip.indexOf(IP_UTILS_FLAG));
        }
        return ip;
    }


    /**
     * 获取 本地IP地址
     *
     * @return 本地IP地址
     */
    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ignored) {
        }
        return "127.0.0.1";
    }


    /**
     * 获取 主机名
     *
     * @return 本地主机名
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
        }
        return "未知";
    }


    /**
     * 此方法描述的是：获得服务器的IP地址(多网卡)
     */
    public static Set<String> getLocalIPS() {
        var ipList = new HashSet<String>();
        try {
            var netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                var ips = netInterfaces.nextElement().getInetAddresses();
                while (ips.hasMoreElements()) {
                    var ip = ips.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().matches("(\\d{1,3}\\.){3}\\d{1,3}")) {
                        ipList.add(ip.getHostAddress());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ipList;
    }


}
