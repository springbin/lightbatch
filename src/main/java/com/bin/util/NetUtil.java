package com.bin.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * �������������
 */
public class NetUtil {

    private static Logger logger = LogManager.getLogger(NetUtil.class);

    private NetUtil(){
        super();
    }

    static String CACHED_LOCALHOST_ADDR;

    /**
     * ��ȡ���������е�IP
     * @return
     */
    public static String getCachedLocalhostAddr(){
        if(CACHED_LOCALHOST_ADDR == null){
            CACHED_LOCALHOST_ADDR = getLocalHostIPAddress();
        }
        return CACHED_LOCALHOST_ADDR;
    }

    /**
     * ��ȡ����IP
     * @return
     */
    public  static String getLocalHostIPAddress(){
        try{
            return InetAddress.getLocalHost().getHostAddress();
        }catch(Exception e){
            try{
                return InetAddress.getByName(null).getHostAddress();
            }catch(Exception ex){
                String[] ips = getAlllocalHostIPAddress(false);
                if(ips.length >0){
                    return ips[0];
                }
                return "localhost";
            }
        }
    }

    /**
     * ȡ�ñ����󶨵�����IP
     * @return
     */
    public static String[] getAlllocalHostIPAddress(){
        logger.info("getAlllocalHostIPAddress");
        return getAlllocalHostIPAddress(true);
    }

    public static String[] getAlllocalHostIPAddress(boolean allIpflag){
        List<String> ips = new ArrayList<>();
        try{
            Enumeration<?> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while(netInterfaces.hasMoreElements()){
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration<?> netAddress = ni.getInetAddresses();
                while(netAddress.hasMoreElements()){
                    ip = (InetAddress) netAddress.nextElement();
                    if(ip != null && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1){
                        String address = ip.getHostAddress();
                        if(allIpflag){
                            ips.add(address);
                        }else{
                            return new String[]{address};
                        }
                    }
                }
            }
        }catch(Exception e){

        }
        return ips.toArray(new String[0]);
    }

    /**
     * ��ȡ����IP
     * @param hostName
     * @return
     */
    public static String getHostIPAddress(String hostName){
        try{
            return InetAddress.getByName(hostName).getHostAddress();
        }catch(Exception e){
            return null;
        }
    }


    /**
     * ��ȡ����������
     * @return
     */
    public static String getLocalHostName() {
        try{
            return InetAddress.getLocalHost().getHostName();
        }catch(UnknownHostException e){
            try{
                return InetAddress.getByName(null).getHostName();
            }catch(UnknownHostException ex){
                return null;
            }
        }
    }

    /**
     * ��ȡ������
     * @param ipv4
     * @return
     */
    public static String getHostName(String ipv4){
        try{
            String[] ips = ipv4.split("[.]");
            if(ips.length == 4){
                return getInetAddress(ipv4).getCanonicalHostName();
            }
        }catch(Exception e){

        }
        return null;
    }

    public static InetAddress getInetAddress(String ipv4) throws UnknownHostException {
        String[] ips = ipv4.split("[.]");
        return InetAddress.getByAddress(new byte[]{
                (byte)Integer.parseInt(ips[0]),
                (byte)Integer.parseInt(ips[1]),
                (byte)Integer.parseInt(ips[2]),
                (byte)Integer.parseInt(ips[3])
        });
    }

    /**
     * �ж�ָ����ip��port��������
     * @param ip
     * @param port
     * @param timeOut
     * @return
     */
    public static boolean cannConnect(String ip,int port,int timeOut){
        if(ip == null || port == 0){
            return false;
        }
        try{
            InetAddress serverAddress = getInetAddress(ip);
            //��֤IP
            if(!checkIP(ip, timeOut)){
                return false;
            }
            //��֤�˿�
            if(!checkPort(serverAddress,port,timeOut)){
                return false;
            }

        }catch (Exception e){
            return false;
        }
        return true;
    }

    private static boolean checkIP(InetAddress address,int timeOut){
        if(address == null){
            return false;
        }
        try{
            return address.isReachable(timeOut);
        }catch (Exception e){
            return false;
        }
    }
    public static boolean checkIP(String ip,int timeOut){
        if(ip == null){
            return false;
        }
        try{
            InetAddress address = getInetAddress(ip);
            return checkIP(address,timeOut);
        }catch(Exception e){
            return false;
        }
    }

    private static boolean checkPort(InetAddress address,int port, int timeOut){
        if(address == null || port == 0){
            return false;
        }
        Socket clientSocket = null;
        InetSocketAddress serverAddressEndpint = null;
        try{
            clientSocket = new Socket();
            serverAddressEndpint = new InetSocketAddress(address,port);
            clientSocket.connect(serverAddressEndpint,timeOut);
            return true;
        }catch(Exception e){
            return false;
        }finally {
            if(clientSocket != null){
                try{
                    clientSocket.close();
                }catch(Exception e){

                }
            }
        }
    }

}
