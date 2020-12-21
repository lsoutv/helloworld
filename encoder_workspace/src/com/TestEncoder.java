package com;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class TestEncoder {
    //字符集：解码和编码
    @Test
    public void Test1() throws CharacterCodingException {
        // 创建简体中文对应的Charset
        Charset cs = Charset.forName("UTF-8");
        //获取cs对象的编码器
        CharsetEncoder charsetEncoder = cs.newEncoder();
        //获取解码器
        CharsetDecoder charsetDecoder = cs.newDecoder();

        //设置一个字符缓冲区并且放入数据
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("一给我里giaogiao");
        charBuffer.flip();

        //编码
        ByteBuffer buf = charsetEncoder.encode(charBuffer);
        for (int i = 0; i < buf.limit(); i++) {
            System.out.println(buf.get());
        }
        //解码
        buf.flip();
        CharBuffer cbf = charsetDecoder.decode(buf);
        System.out.println(cbf.toString());

    }

}
