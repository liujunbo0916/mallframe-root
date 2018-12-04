package com.easaa.core.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.easaa.entity.ImageFormat;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

/** 
 * 说明：常用工具
 * 创建人：FH Q371855779
 * 修改时间：2015年11月24日
 * @version
 */
public class EATools {
	
	/**
	 * 随机生成六位数验证码 
	 * @return
	 */
	public static int getRandomNum(){
		 Random r = new Random();
		 return r.nextInt(900000)+100000;//(Math.random()*(999999-100000)+100000)
	}
	
	/**
	 * 检测字符串是否不为空(null,"","null")
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(String s){
		return s!=null && !"".equals(s) && !"null".equals(s);
	}
	
	/**
	 * 检测字符串是否为空(null,"","null")
	 * @param s
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean isEmpty(String s){
		return s==null || "".equals(s) || "null".equals(s);
	}
	
	/**
	 * 字符串转换为字符串数组
	 * @param str 字符串
	 * @param splitRegex 分隔符
	 * @return
	 */
	public static String[] str2StrArray(String str,String splitRegex){
		if(isEmpty(str)){
			return null;
		}
		return str.split(splitRegex);
	}
	
	/**
	 * 用默认的分隔符(,)将字符串转换为字符串数组
	 * @param str	字符串
	 * @return
	 */
	public static String[] str2StrArray(String str){
		return str2StrArray(str,",\\s*");
	}
	
	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String date2Str(Date date){
		return date2Str(date,"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
	 * @param date
	 * @return
	 */
	public static Date str2Date(String date){
		if(notEmpty(date)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return new Date();
		}else{
			return null;
		}
	}
	
	/**
	 * 按照yyyy-MM-dd的格式，字符串转日期
	 * @param date
	 * @return
	 */
	public static String str2YMD(String dateStr){
		if(notEmpty(dateStr)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return date2Str(sdf.parse(dateStr),"yyyy-MM-dd");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return date2Str(new Date(),"yyyy-MM-dd");
		}else{
			return null;
		}
	}
	
	
	/**
	 * 按照参数format的格式，日期转字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2Str(Date date,String format){
		if(date!=null){
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		}else{
			return "";
		}
	}
	
	/**
	 * 把时间根据时、分、秒转换为时间段
	 * @param StrDate
	 */
	public static String getTimes(String StrDate){
		String resultTimes = "";
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    java.util.Date now;
	    
	    try {
	    	now = new Date();
	    	java.util.Date date=df.parse(StrDate);
	    	long times = now.getTime()-date.getTime();
	    	long day  =  times/(24*60*60*1000);
	    	long hour = (times/(60*60*1000)-day*24);
	    	long min  = ((times/(60*1000))-day*24*60-hour*60);
	    	long sec  = (times/1000-day*24*60*60-hour*60*60-min*60);
	        
	    	StringBuffer sb = new StringBuffer();
	    	//sb.append("发表于：");
	    	if(hour>0 ){
	    		sb.append(hour+"小时前");
	    	} else if(min>0){
	    		sb.append(min+"分钟前");
	    	} else{
	    		sb.append(sec+"秒前");
	    	}
	    		
	    	resultTimes = sb.toString();
	    } catch (ParseException e) {
	    	e.printStackTrace();
	    }
	    
	    return resultTimes;
	}
	
	/**
	 * 写txt里的单行内容
	 * @param filePath  文件路径
	 * @param content  写入的内容
	 */
	public static void writeFile(String fileP,String content){
		String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""))+"../../";	//项目路径
		filePath = (filePath.trim() + fileP.trim()).substring(6).trim();
		if(filePath.indexOf(":") != 1){
			filePath = File.separator + filePath;
		}
		try {
	        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(filePath),"utf-8");      
	        BufferedWriter writer=new BufferedWriter(write);          
	        writer.write(content);      
	        writer.close(); 

	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	  * 验证邮箱
	  * @param email
	  * @return
	  */
	 public static boolean checkEmail(String email){
	  boolean flag = false;
	  try{
	    String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	    Pattern regex = Pattern.compile(check);
	    Matcher matcher = regex.matcher(email);
	    flag = matcher.matches();
	   }catch(Exception e){
	    flag = false;
	   }
	  return flag;
	 }
	
	 /**
	  * 验证手机号码
	  * @param mobiles
	  * @return
	  */
	 public static boolean checkMobileNumber(String mobileNumber){
	  boolean flag = false;
	  try{
	    Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
	    Matcher matcher = regex.matcher(mobileNumber);
	    flag = matcher.matches();
	   }catch(Exception e){
	    flag = false;
	   }
	  return flag;
	 }
	 
	/**
	 * 检测KEY是否正确
	 * @param paraname  传入参数
	 * @param FKEY		接收的 KEY
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean checkKey(String paraname, String FKEY){
		paraname = (null == paraname)? "":paraname;
		return MD5.md5(paraname+EADate.getDateStr(null)+",easaa,").equals(FKEY);
	}
	 
	/**
	 * 读取txt里的单行内容
	 * @param filePath  文件路径
	 */
	public static String readTxtFile(String fileP) {
		try {
			
			String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""))+"../../";	//项目路径
			filePath = filePath.replaceAll("file:/", "");
			filePath = filePath.replaceAll("%20", " ");
			filePath = filePath.trim() + fileP.trim();
			if(filePath.indexOf(":") != 1){
				filePath = File.separator + filePath;
			}
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { 		// 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
				new FileInputStream(file), encoding);	// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					return lineTxt;
				}
				read.close();
			}else{
				System.out.println("找不到指定的文件,查看此路径是否正确:"+filePath);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
		}
		return "";
	}
	
	/**
	 * 
	 * 获取邀请码
	 */
	public static String getRegisterCode(int num,String type) {
		Random random = new Random(); 
		String randString = "";
		if("0".equals(type)){
			if(num>9){
				num = 9;
			}
			randString = "0123456789";
		}else if("1".equals(type)){
			randString = "abcdefghijklmnopqrstuvwxyz";
		}else if("2".equals(type)){
			randString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		}else if("3".equals(type)){
			randString = "0123456789abcdefghijklmnopqrstuvwxyz";
		}else{
//			randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			randString = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
		}
		String RegisterCode = "";
		while (RegisterCode.length()<num) {
			String rand = String.valueOf(getRandomString(random.nextInt(randString 
					.length()),randString));
			if(!RegisterCode.contains(rand)){
				RegisterCode+=rand;
			}
		}
		return RegisterCode;
	}
	
	/**
	 * 获取订单流水号
	 */
    public static String getOrderSN() { 
        return EADate.getOrderSnString()+EAString.getRandomString(2); 
    } 
	
	/**
	 * 获取随机的字符
	 */
    public static String getRandomString(int num,String s) { 
        return String.valueOf(s.charAt(num)); 
    } 
    

	 //地球平均半径  
    private static final double EARTH_RADIUS = 6378137;  
    //把经纬度转为度（°）  
    private static double rad(double d){  
       return d * Math.PI / 180.0;  
    }  
      
    /**  
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米  
     * @param lng1  
     * @param lat1  
     * @param lng2  
     * @param lat2  
     * @return  
     */  
    public static double getDistance(double lng1, double lat1, double lng2, double lat2){  
       double radLat1 = rad(lat1);  
       double radLat2 = rad(lat2);  
       double a = radLat1 - radLat2;  
       double b = rad(lng1) - rad(lng2);  
       double s = 2 * Math.asin(  
            Math.sqrt(  
                Math.pow(Math.sin(a/2),2)   
                + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)  
            )  
        );  
       s = s * EARTH_RADIUS;  
       s = Math.round(s * 10000) / 10000;  
       return s;  
    }  
	
    /**
     * 图片上传   对图片进行 放大缩小剪裁等处理
     * destImg 待处理的图片
     * imageFormat 处理的规格
     * @throws IOException 
     * 
     */
    public static  void dealWithImgs(MultipartFile destImg,ImageFormat[] imageFormat) throws IOException{
    	CommonsMultipartFile cf = (CommonsMultipartFile) destImg;
		DiskFileItem fi = (DiskFileItem) cf.getFileItem();
		File distFile = fi.getStoreLocation();
		String distPath = 
				distFile.getPath().substring(0, distFile.getPath().lastIndexOf("\\") + 1);
		String suffix = destImg.getOriginalFilename().substring(destImg.getOriginalFilename().lastIndexOf(".") + 1);
		for(ImageFormat tImageFormat : imageFormat){
			tImageFormat.setSuffix(suffix);
			try{ //如果转换失败 默认生成几张和原图一样的名字不同的图片
			     fixedImage(distFile, tImageFormat, new File(distPath+tImageFormat.getFileName()+tImageFormat.getFlag()+"."+suffix));
			}catch(Exception e){
				e.printStackTrace();
				File tempFile = new File(distPath+tImageFormat.getFlag()+"."+suffix);
				FileUtils.copyFile(distFile,tempFile , false);
				tImageFormat.setFile(tempFile);
			}
		}
    }
    /**
     * 图片上传   对图片进行 放大缩小剪裁等处理
     * destImg 待处理的图片
     * imageFormat 处理的规格
     * @throws IOException 
     */
    public static  void dealWithImgs(File destImg,ImageFormat[] imageFormat) throws IOException{
		File distFile = destImg;
		String distPath = 
				distFile.getPath().substring(0, distFile.getPath().lastIndexOf("."));
		String suffix = destImg.getAbsolutePath().substring(destImg.getAbsolutePath().lastIndexOf(".") + 1);
		for(ImageFormat tImageFormat : imageFormat){
			tImageFormat.setSuffix(suffix);
			try{ //如果转换失败 默认生成几张和原图一样的名字不同的图片
			     fixedImage(distFile, tImageFormat, new File(distPath+tImageFormat.getFlag()+"."+suffix));
			}catch(Exception e){
				e.printStackTrace();
				File tempFile = new File(distPath+tImageFormat.getFlag()+"."+suffix);
				FileUtils.copyFile(distFile,tempFile , false);
				tImageFormat.setFile(tempFile);
			}
		}
    }
    /**
     * 固定图片大小  保持图片不变形 
     */
    private static void fixedImage(File distFile,ImageFormat imageFormat,File targetFile){
    	try{
    		BufferedImage image = ImageIO.read(distFile);  
    		Builder<BufferedImage> builder = null;  
    		  
    		int imageWidth = image.getWidth();  
    		int imageHeitht = image.getHeight();  
    		if ( (float)imageFormat.getHeight() / imageFormat.getWidth() != (float)imageWidth / imageHeitht) {  
    		    if (imageWidth > imageHeitht) {  
    		        image = Thumbnails.of(distFile).height((int)imageFormat.getHeight()).asBufferedImage();  
    		    } else {  
    		        image = Thumbnails.of(distFile).width((int)imageFormat.getWidth()).asBufferedImage();  
    		    }  
    		    builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, (int)imageFormat.getWidth(), (int)imageFormat.getHeight()).size((int)imageFormat.getWidth(), (int)imageFormat.getHeight());  
    		} else {  
    		    builder = Thumbnails.of(image).size((int)imageFormat.getWidth(), (int)imageFormat.getHeight());  
    		}  
    		//outputQuality 输出的图片质量  
    		builder.outputQuality(0.5f).toFile(targetFile); 
    		imageFormat.setFile(targetFile);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public static void main(String[] args) throws IOException {
    	/*ImageFormat[] imageFormats = new ImageFormat[]{new ImageFormat(750, 601, "detail"),
				new ImageFormat(226, 226, "list"),
				new ImageFormat(328, 328, "listbig"),
				new ImageFormat(238, 238, "listmiddle"),
				new ImageFormat(175, 175, "listsmall")
				}; //网站图片规格
    	
    	 *文件夹下面的处理方式
    	 
    	  File parentFile = new File("D:\\images\\serverimg\\product\\2017");
    	System.out.println(parentFile.getPath());
    	List<File> images  = new ArrayList<>();
    	findFile(parentFile, images);
    	for(File f : images){
    		System.out.println("处理图片名字为<>><><><<>><><><"+f.getName());
    			dealWithImgs(f, imageFormats);
    	}*/
    	
    	File file = new File("D:\\images\\28\\1493376912707EsLYqVN1.png");
    	FileUtils.copyFile(file, new File("D:\\images\\28\\1493376912707EsLYqVN2.png"), false);
    	/**
    	 * 单张图片处理
    	 */
    	/*File parentFile = new File("D:\\images\\28\\1493376912707EsLYqVN.png");
    	dealWithImgs(parentFile, imageFormats);*/
	}
    public static void findFile(File file,List<File> images){
    	if(file.isDirectory()){
    		File[] files = file.listFiles();
    		for(File tFile : files){
    			findFile(tFile,images);
    		}
    	}else{
    		images.add(file);
    	}
    }
}