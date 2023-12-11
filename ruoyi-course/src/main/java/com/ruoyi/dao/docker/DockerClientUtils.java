
package com.ruoyi.dao.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.core.DockerClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;


/**
 * @Auther:
 * @Date: 2023/7/19
 * @Description: Java API实现创建Docker容器...
 */
public class DockerClientUtils {

    private Logger logger= LoggerFactory.getLogger(DockerClientUtils.class);


    /**
     * 连接Docker服务器
     * @return
     */
    public DockerClient connectDocker(String dockerInstance){
        DockerClient dockerClient = DockerClientBuilder.getInstance(dockerInstance).build();
        dockerClient.infoCmd().exec();
        return dockerClient;
    }

    /**
     * 创建容器
     * @param client
     * @return
     */
    public CreateContainerResponse createContainers(DockerClient client, String containerName, String imageName,Integer hostPort){
        //映射端口 hostPort(一个范围内的随机数) —> 22
        ExposedPort tcp22 = ExposedPort.tcp(22);
        Ports portBindings = new Ports();
//        Integer hostPort=generateRandomNumber();      // 之后该端口号要写进数据库
        logger.info("当前ip端口为:{}",hostPort);
        portBindings.bind(tcp22, Ports.Binding.bindPort(hostPort));

        CreateContainerResponse container = client.createContainerCmd(imageName)
                .withName(containerName)
                .withHostConfig(newHostConfig().withPortBindings(portBindings))
                .withPrivileged(true)
                .withRestartPolicy(RestartPolicy.alwaysRestart())
                .withExposedPorts(tcp22).exec();
        return container;
    }

    /**
     * 创建容器
     *
     * @param client
     * @return
     */
    public CreateContainerResponse createContainers(DockerClient client, String containerName, String imageName, Map<Integer, Integer> portMap) {
        // 端口绑定

        Iterator<Map.Entry<Integer, Integer>> iterator = portMap.entrySet().iterator();
        List<PortBinding> portBindingList = new ArrayList<>();
        List<ExposedPort> exposedPortList = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            ExposedPort tcp = ExposedPort.tcp(entry.getKey());
            Ports.Binding binding = Ports.Binding.bindPort(entry.getValue());
            PortBinding ports = new PortBinding(binding, tcp);
            portBindingList.add(ports);
            exposedPortList.add(tcp);
        }


        CreateContainerResponse container = client.createContainerCmd(imageName)
                .withName(containerName)
                .withHostConfig(newHostConfig().withPortBindings(portBindingList))
                .withPrivileged(true)
                .withExposedPorts(exposedPortList).exec();

        return container;
    }

    /**
     * 启动容器
     * @param client
     * @param containerId
     */
    public void startContainer(DockerClient client,String containerId){
        client.startContainerCmd(containerId).exec();
    }

    /**
     * 启动容器
     * @param client
     * @param containerId
     */
    public void stopContainer(DockerClient client,String containerId){
        client.stopContainerCmd(containerId).exec();
    }

    /**
     * 删除容器
     * @param client
     * @param containerId
     */
    public void removeContainer(DockerClient client,String containerId){
        client.removeContainerCmd(containerId).exec();
    }

    /*
        生成 1024到65535之间的随机数
     */
    public static Integer generateRandomNumber() {
        int min = 1024;
        int max = 65535;
        int randomNumber = (int)(Math.random() * ((max - min) + 1)) + min;
        return randomNumber;
    }


    public static void main(String[] args){
        DockerClientUtils dockerClientUtils =new DockerClientUtils();
        //连接Docker服务器
        DockerClient client = dockerClientUtils.connectDocker("tcp://192.168.0.18:2375");

//        //创建map对象
//        Map<Integer,Integer> portmap = new HashMap<Integer,Integer>();       //数据采用的哈希表结构
//        //给map中添加元素
        Integer hostPort = generateRandomNumber();
//        portmap.put(22,hostPort);
//        System.out.println("Your exposed port: "+ hostPort);

        //创建容器
        CreateContainerResponse container = dockerClientUtils.createContainers(client,"openeulerTest1","openeuler_general:20.03",hostPort);
        //启动容器
        dockerClientUtils.startContainer(client,container.getId());

//        //创建容器
//        CreateContainerResponse container = dockerClientUtils.createContainers(client,"openeulerTest2","openeuler_general:20.03");
//        //启动容器
//        dockerClientUtils.startContainer(client,container.getId());
    }
}