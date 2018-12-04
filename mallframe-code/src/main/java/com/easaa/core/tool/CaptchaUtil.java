package com.easaa.core.tool;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Created by junbo
 * on 2018/12/1
 */
public class CaptchaUtil {

    private CaptchaUtil(){}

    private static final char[] CHARS = { '2', '3', '4', '5', '6', '7', '8',
            '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z','a','b','c','d','e',
            'f','g','h','i','j','k','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    private static Random random = new Random();

    public  static String getRandomString(int n)
    {
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < n; i++)
        {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }
    private static Color getRandomColor()
    {
        return new Color(random.nextInt(255),random.nextInt(255),
                random.nextInt(255));
    }
    private static Color getReverseColor(Color c)
    {
        return new Color(255 - c.getRed(), 255 - c.getGreen(),
                255 - c.getBlue());
    }

    public static void outputCaptcha(String randomString,HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("image/jpeg");
        int width = 110;
        int height = 34;
        Color color = getRandomColor();
        Color reverse = getReverseColor(color);

        BufferedImage bi = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.setColor(reverse);
        g.drawString(randomString, 8, 30);
        for (int i = 0, n = random.nextInt(100); i < n; i++)
        {
            g.drawRect(random.nextInt(width), random.nextInt(height), 1, 1);
        }
        // 转成JPEG格式
        ServletOutputStream out = response.getOutputStream();
   /*  JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
    encoder.encode(bi);*/
        ImageIO.write(bi,"jpeg",out);
    }

}
