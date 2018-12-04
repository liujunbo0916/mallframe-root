package com.test.collections;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.junit.Test;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;
/**
 * 图片处理类
 * @author liujunbo
 */
public class ThumbnailatorTest {
	/**
	 * 对图片尺寸进行重设
	 * 第一个参数为原图片路径
	 * 第二个参数为输出图片
	 * @throws IOException
	 */
	/*@Test
	public void rechangeImage() throws IOException {
	    Thumbnails.of(new File("D:\\images\\111.jpg"))
	              .size(150, 80)可以自定义图片的大小 
	              .keepAspectRatio(false) 不按照比例 按照尺寸
	              .scale(0.5f) //可以对原图片进行缩放
	              .toFile(new File("D:\\images\\111-1.jpg"));
	}*/
	
	/**
	 * 对图片尺寸进行重设，并且在中间添加水印（水印为图片）
	 * 第一个参数为原图片
	 * 第二个参数为 水印参数
	 * 第三个参数为输出图片
	 */
	/*@Test
	public void rechangeImageWatermark() throws IOException {
	    Thumbnails.of(new File("D:\\images\\反面.jpg"))
	              .size(480, 480)
	              .watermark(Positions.CENTER,
	                         ImageIO.read(new File("D:\\images\\111-1.jpg")), 0.8f)
	              .toFile(new File("D:\\images\\反面1.jpg"));
	}*/
	/**
	 * 对图片尺寸进行重设，并且在中间添加水印（水印为自定义文字）
	 * 第一个参数为原图片
	 * 第二个参数为 水印参数
	 * 第三个参数为输出图片
	 */
/*	@Test
	public void rechangeImageWordWatermark() throws IOException {
	    //根据文字自定义出一张水印图片
	    BufferedImage bi = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = bi.createGraphics();
	    g.setColor(Color.LIGHT_GRAY);
	    g.drawRect(0, 0, 64, 128);
	    char[] data = "www.iguoshifeng.com".toCharArray();
	    g.drawChars(data, 0, data.length, 5, 32);
	    Thumbnails.of(new File("D:\\images\\反面.jpg"))
	              .scale(0.5f)
	              .watermark(Positions.BOTTOM_RIGHT, bi, 0.2f)
	              .toFile(new File("D:\\images\\反面-2.jpg"));
	}*/
	/**
	 * 图片旋转
	 * @throws IOException
	 * rotate 旋转度数  90度
	 */
	/*@Test
	public void rotate() throws IOException {
	    Thumbnails.of(new File("D:\\images\\正面.jpg"))
	              .scale(0.5f)
	              .rotate(90.0f)
	              .toFile(new File("D:\\images\\正面-1.jpg"));
	}*/
	/**
	 * 批量处理图片
	 * 对图片批量进行缩放  剪裁
	 */
	/*@Test
	public void batchDw() throws IOException {
	    File dir = new File("E:\\test\\");
	    File[] fs = dir.listFiles();
	    Thumbnails.of(fs)
	              .scale(0.8f)
	              .outputFormat("jpg")
	              .outputQuality(1.0f)
	              .toFiles(new File("E:\\test\\"), Rename.PREFIX_HYPHEN_THUMBNAIL);
	}
	*/
	/**
	 * 图片剪裁
	 * @throws IOException 
	 */
/*	@Test
	public void cutImages() throws IOException{
		Thumbnails.of(new File("D:\\images\\正面.jpg")).sourceRegion(Positions.CENTER,150,150)  
		.size(300,300).toFile(new File("D:\\images\\正面-222.jpg"));  
	}*/
	
	/**
	 * 
	 * 压缩至指定图片尺寸，保持图片不变形，多余部分裁剪掉
	 * @throws IOException 
	 * 
	 */
/*	@Test
	public void fixedSize() throws IOException{
		BufferedImage image = ImageIO.read(new File("D:\\images\\111.jpg"));  
		Builder<BufferedImage> builder = null;  
		  
		int imageWidth = image.getWidth();  
		int imageHeitht = image.getHeight();  
		if ( (float)500 / 800 != (float)imageWidth / imageHeitht) {  
		    if (imageWidth > imageHeitht) {  
		        image = Thumbnails.of(new File("D:\\images\\111.jpg")).height(500).asBufferedImage();  
		    } else {  
		        image = Thumbnails.of(new File("D:\\images\\111.jpg")).width(800).asBufferedImage();  
		    }  
		    builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, 800, 500).size(800, 500);  
		} else {  
		    builder = Thumbnails.of(image).size(200, 100);  
		}  
		builder.outputFormat("jpg").toFile(new File("D:\\images\\111222.jpg"));  
	}
	*/
	
	public static void main(String[] args) {
		
		String savePath  = "/mallframe/goods/2017/06/20/20323143243544545.png";
		savePath  = savePath.substring(savePath.lastIndexOf("/")+1, savePath.lastIndexOf("."));
		System.out.println(savePath);
	}
	
	
}
