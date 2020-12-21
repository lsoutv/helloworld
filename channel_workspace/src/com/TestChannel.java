package com;

import org.junit.Test;
import sun.nio.ch.ChannelInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TestChannel {
    //利用通道完成数据传输(直接缓冲区)
    @Test
    public void Test3() throws IOException {
        FileChannel inchannel = FileChannel.open(Paths.get("abc.txt"),StandardOpenOption.READ);
        FileChannel outchannel = FileChannel.open(Paths.get("abc3.txt"),StandardOpenOption.WRITE,
                StandardOpenOption.READ,StandardOpenOption.CREATE);
        inchannel.transferTo(0,inchannel.size(),outchannel);
        outchannel.close();
        inchannel.close();
    }

    //利用通道完成文件复制(直接缓冲区)
    @Test
    public void Test2() throws IOException {
        FileChannel inchannel = null;
        FileChannel outchannel = null;
        MappedByteBuffer inmap = null;
        MappedByteBuffer outmap = null;
        try{
            inchannel = FileChannel.open(Paths.get("abc.txt"), StandardOpenOption.READ);
            outchannel = FileChannel.open(Paths.get("abc2.txt"),StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE,StandardOpenOption.READ);
            inmap = inchannel.map(FileChannel.MapMode.READ_ONLY,0,inchannel.size());
            outmap = outchannel.map(FileChannel.MapMode.READ_WRITE,0,inchannel.size());
            //直接对缓冲区进行数据的读写操作
            byte[] bytes = new byte[inmap.limit()];
            inmap.get(bytes);
            outmap.put(bytes);

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if (outchannel!=null){
                    outchannel.close();
                }
                if (inchannel!=null){
                    inchannel.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //利用通道完成文件复制(非直接缓冲区)
    @Test
    public void Test1(){
        FileInputStream is = null;
        FileOutputStream os = null;
        FileChannel inchannel1 = null;
        FileChannel outchannel2 = null;

        try{
            is = new FileInputStream(new File("abc.txt"));
            os = new FileOutputStream(new File("abc1.txt"));
            //获取通道
            inchannel1 = is.getChannel();
            outchannel2 = os.getChannel();
            //创建一个缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);
            //把通道中的数据写入缓冲区
            int len = -1;
            while((len=inchannel1.read(buf))!=-1){
                //切换成读取模式
                buf.flip();
                //把缓冲区中的数据写入到channel2中
                outchannel2.write(buf);
                //清空缓冲区
                buf.clear();
            }
            System.out.println("拷贝成功");
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            try{
                if (null!=outchannel2){
                    outchannel2.close();
                }
                if (null!=inchannel1){
                    inchannel1.close();
                }
                if (null!=os){
                    os.close();
                }
                if (null!=is){
                    is.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
