package com.easaa.user.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EATools;
import com.easaa.core.util.EAUtil;
import com.easaa.core.util.FtpUtil;
import com.easaa.entity.ImageFormat;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.user.dao.CircleMapper;
import com.easaa.user.entity.CircleComment;

@Service
public class CircleService extends EABaseService {
	@Autowired
	private CircleMapper circleMapper;

	@Override
	public EADao getDao() {
		return circleMapper;
	}

	/**
	 * 圈子列表（所有）
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PageData> selectCirclelist(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("currentPage"))) {
			throw new Exception("页数为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("circle_type"))) {
			throw new Exception("请求类型不能为空");
		}
		Page page = new Page();
		page.setApp(true);
		page.setCurrentPage(Integer.parseInt(pd.getAsString("currentPage")));
		page.setShowCount(5);
		List<PageData> data = new ArrayList<PageData>();
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			pd.put("users_id", "0");
		}else{
			pd.put("users_id", pd.getAsString("user_id"));
		}
		if (pd.getAsString("circle_type").equals("0")) {
			pd.put("user_id", "");
		}
		pd.remove("circle_type");
		page.setPd(pd);
		data = circleMapper.selectCircleByUidPage(page);
	/*	if (EAUtil.isEmpty(pd.getAsInt("user_id"))) {
			pd.put("user_id", "0");
			pd.remove("circle_type");
			page.setPd(pd);
			data = circleMapper.selectCircleByPage(page);
		} else {
			int userId = pd.getAsInt("user_id");
			// 全部
			if (pd.getAsString("circle_type").equals("0")) {
				pd.put("user_id", "0");
				pd.remove("circle_type");
			} else if (pd.getAsString("circle_type").equals("1")) {
				pd.remove("circle_type");
			}
			page.setPd(pd);
			data = circleMapper.selectCircleByPage(page);
			for (PageData pageData : data) {
				PageData tupd = new PageData();
				tupd.put("user_id", userId);
				tupd.put("circle_id", pageData.getAsString("circle_id"));
				PageData tup = circleMapper.selectThumbsupById(tupd);
				if (EAUtil.isEmpty(tup)) {
					pageData.put("is_liked", "0");
				} else {
					pageData.put("is_liked", 1);
				}
			}
		}*/
		return data;
	}

	/**
	 * 圈子发布时，关联的商品，课程列表
	 * @return
	 * @throws Exception
	 */
	public List<PageData> selectGoodsCourse(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("currentPage"))) {
			throw new Exception("页数为空");
		}
		Page page = new Page();
		page.setApp(true);
		page.setCurrentPage(Integer.parseInt(pd.getAsString("currentPage")));
		page.setShowCount(15);
		page.setPd(pd);
		List<PageData> circlelist = circleMapper.selectCircleByPage(page);
		return circlelist;
	}

	/**
	 * 圈子评论，回复列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public PageData selectCircleComment(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("circle_id"))) {
			throw new Exception("circle_id不能为空，你要搞啥");
		}
		PageData data = new PageData();
		PageData circle = circleMapper.selectCircleById(pd.getAsInt("circle_id"));
		circle.put("circle_time", EADate.prettyTime(circle.getAsString("circle_time")));
		data.put("circle", circle);
		List<CircleComment> commentlist = commentList(pd);
		data.put("commentlist", commentlist);
		return data;
	}

	/**
	 * 更多里面的评论
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PageData> selectCircleCommentMore(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id不能为空啊，你搞的什么玩意。。。");
		}
		if (EAUtil.isEmpty(pd.getAsString("currentPage"))) {
			throw new Exception("页数不能为空啊，你搞的什么玩意。。。");
		}
		Page page = new Page();
		page.setApp(true);
		page.setCurrentPage(Integer.parseInt(pd.getAsString("currentPage")));
		page.setShowCount(15);
		page.setPd(pd);
		List<PageData> list = circleMapper.selectCirclecommentMyPage(page);
		for (PageData pageData : list) {
			pageData.put("comment_context_time", EADate.prettyTime(pageData.getAsString("comment_context_time")));
			PageData circle = circleMapper.selectCircleById(pageData.getAsInt("circle_id"));
			pageData.put("circle_imgs", circle.getAsString("circle_imgs"));
		}
		return list;
	}

	/**
	 * 圈子点赞
	 * 
	 * @return
	 * @throws Exception
	 */
	public PageData givethumbsUp(PageData pd) throws Exception {
		PageData data = new PageData();
		PageData tup = circleMapper.selectThumbsupById(pd);
		if (EAUtil.isEmpty(tup)) {
			Page page = new Page();
			PageData pdc = new PageData();
			pdc.put("circle_id", pd.getAsString("circle_id"));
			page.setPd(pdc);
			List<PageData> list = circleMapper.selectCircleByPage(page);
			if (EAUtil.isNotEmpty(list) && list.size() > 0) {
				pdc.put("circle_praise_total", list.get(0).getAsInt("circle_praise_total") + 1);
				int upc = circleMapper.updateCircle(pdc);
				if (upc > 0) {
					circleMapper.insertThumbsup(pd);
					data.put("number", list.get(0).getAsInt("circle_praise_total") + 1);
				} else {
					throw new Exception("你搞的什么玩意。。。导致系统错误");
				}
			}
		} else {
			throw new Exception("你已经点过赞了，别瞎几把乱点！");
		}
		return data;
	}

	/**
	 * 圈子评论回复
	 * 
	 * @return (circle_id,p_id,comment_context) p_id 评论0 回复 comment_id circle_id
	 *         圈子id comment_context 评论回复内容
	 * @throws Exception
	 */
	public boolean circleComment(PageData pd) throws Exception {
		int incc = circleMapper.insertCirclecomment(pd);
		if (incc > 0) {
			if (pd.getAsString("p_id").equals("0")) {
				Page page = new Page();
				PageData pdc = new PageData();
				pdc.put("circle_id", pd.getAsString("circle_id"));
				page.setPd(pdc);
				List<PageData> list = circleMapper.selectCircleByPage(page);
				if (EAUtil.isNotEmpty(list) && list.size() > 0) {
					pdc.put("circle_comment_total", list.get(0).getAsInt("circle_comment_total") + 1);
					int upc = circleMapper.updateCircle(pdc);
					if (upc > 0) {
						return true;
					} else {
						throw new Exception("你搞的什么玩意。。。导致系统错误");
					}
				} else {
					throw new Exception("你搞的什么玩意。。。导致系统错误");
				}
			} else {
				return true;
			}
		} else {
			throw new Exception("你搞的什么玩意。。。导致系统错误");
		}
	}

	/**
	 * 圈子发布
	 * 
	 * @return
	 * @throws Exception
	 *             user_id circle_imgs circle_msg circle_isrelevance
	 *             circle_relevance_type circle_product_id
	 */
	@SuppressWarnings("restriction")
	public boolean insertcircle(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id不能为空");
		}
		if (EAUtil.isNotEmpty(pd.getAsString("circle_imgs"))) {
			byte[] bt = null;
			String[] imgs = pd.getAsString("circle_imgs").split(",");
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			String savePath = "";
			for (int i = 0; i < imgs.length; i++) {
				String fileName = EADate.getCurrentTimeAsNumber() + "" + i + ".jpg";
				bt = decoder.decodeBuffer(imgs[i]);
				savePath += FtpUtil.upload(bt, "mallframe", "circle",
						fileName) + ",";
			    //对原图进行剪裁
				try{
					ImageFormat[] imageFormats = new ImageFormat[]{new ImageFormat(328, 328, "clistbig")}; //网站图片规格
					File file = new File("/"+fileName);
					if (file.exists())
					{
					  file.delete();
					}
					FileOutputStream fileout = new FileOutputStream(file);
					fileout.write(bt, 0, bt.length);
					EATools.dealWithImgs(file, imageFormats);
					for(ImageFormat tif : imageFormats){
						tif.setFileName(savePath.substring(savePath.lastIndexOf("/")+1, savePath.lastIndexOf(".")));
						FtpUtil.upload(tif, "mallframe", "circle");
						//上传之后删除文件
						tif.getFile().delete();
					}
					//上传之后删除原文件
					file.delete();
				}catch(Exception e){
				}
			}
			pd.put("circle_imgs", savePath.subSequence(0, (savePath.length() - 1)));
		}
		pd.put("circle_time", EADate.getCurrentTime());
		pd.put("circle_praise_total", "0");
		pd.put("circle_comment_total", "0");
		int inserts = circleMapper.insertCircle(pd);
		if (inserts > 0) {
			return true;
		} else {
			throw new Exception("系统出错了");
		}
	}
	/**
	 * 圈子删除
	 * @return
	 * @throws Exception
	 */
	public boolean deletCeircle(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id不能为空啊");
		}
		if (EAUtil.isEmpty(pd.getAsString("circle_id"))) {
			throw new Exception("circle_id不能为空啊");
		}
		PageData data = circleMapper.selectCircleById(pd.getAsInt("circle_id"));
		if (pd.getAsString("user_id").equals(data.getAsString("user_id"))) {
			circleMapper.deleteCirclecomment(pd);
			int inc = circleMapper.deleteCircle(pd);
			if (inc > 0) {
				return true;
			} else {
				throw new Exception("删除圈子出错");
			}
		} else {
			throw new Exception("不是你的圈子");
		}
	}

	/**
	 * 圈子评论删除
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean deleteComment(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("circle_id"))) {
			throw new Exception("circle_id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("comment_id"))) {
			throw new Exception("comment_id不能为空");
		}
		String comment_ids = pd.getAsString("comment_id");
		List<CircleComment> commentlist = commentList(pd);
		for (CircleComment pageData : commentlist) {
			comment_ids += "," + pageData.getComment_id();
		}
		PageData dpd = new PageData();
		dpd.put("comment_ids", comment_ids);
		int incback = circleMapper.deleteCommentback(dpd);
		if (incback > 0) {
			Page page = new Page();
			PageData pdc = new PageData();
			pdc.put("circle_id", pd.getAsString("circle_id"));
			page.setPd(pdc);
			List<PageData> list = circleMapper.selectCircleByPage(page);
			if (EAUtil.isNotEmpty(list) && list.size() > 0) {
				pdc.put("circle_comment_total", list.get(0).getAsInt("circle_comment_total") - 1);
				int upc = circleMapper.updateCircle(pdc);
				if (upc > 0) {
					return true;
				} else {
					throw new Exception("你搞的什么玩意。。。导致系统错误");
				}
			} else {
				throw new Exception("你搞的什么玩意。。。导致系统错误");
			}
		} else {
			throw new Exception("你搞的什么玩意。。。导致系统错误");
		}
	}
	/**
	 * 圈子删除（后台）
	 * @return
	 * @throws Exception
	 */
	public boolean delCeircle(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("circle_id"))) {
			throw new Exception("circle_id不能为空");
		}
		PageData data = circleMapper.selectCircleById(pd.getAsInt("circle_id"));
		if(EAUtil.isEmpty(data)){
			throw new Exception("圈子不存在");
		}
		circleMapper.deleteCirclecomment(pd);
		int inc = circleMapper.deleteCircle(pd);
		if (inc > 0) {
			return true;
		} else {
			throw new Exception("删除圈子出错");
		}
	}

	/**
	 * 圈子评论删除（后台）
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean delComment(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("circle_id"))) {
			throw new Exception("circle_id不能为空啊，你搞的什么玩意。。。");
		}
		if (EAUtil.isEmpty(pd.getAsString("comment_id"))) {
			throw new Exception("comment_id不能为空啊，你搞的什么玩意。。。");
		}
		String comment_ids = pd.getAsString("comment_id");
		List<CircleComment> commentlist = commentList(pd);
		for (CircleComment pageData : commentlist) {
			comment_ids += "," + pageData.getComment_id();
		}
		PageData dpd = new PageData();
		dpd.put("comment_ids", comment_ids);
		int incback = circleMapper.deleteCommentback(dpd);
		if (incback > 0) {
			Page page = new Page();
			PageData pdc = new PageData();
			pdc.put("circle_id", pd.getAsString("circle_id"));
			page.setPd(pdc);
			List<PageData> list = circleMapper.selectCircleByPage(page);
			if (EAUtil.isNotEmpty(list) && list.size() > 0) {
				pdc.put("circle_comment_total", list.get(0).getAsInt("circle_comment_total") - 1);
				int upc = circleMapper.updateCircle(pdc);
				if (upc > 0) {
					return true;
				} else {
					throw new Exception("你搞的什么玩意。。。导致系统错误");
				}
			} else {
				throw new Exception("你搞的什么玩意。。。导致系统错误");
			}
		} else {
			throw new Exception("你搞的什么玩意。。。导致系统错误");
		}
	}

	/**
	 * 递归 评论列表
	 * 
	 * @param pd
	 * @return
	 */
	public List<CircleComment> commentList(PageData pd) {
		pd.put("p_id", "0");
		// 根据圈子的id查一级评论
		List<CircleComment> comlist = new ArrayList<CircleComment>();
		List<CircleComment> result = new ArrayList<CircleComment>();
		for (PageData pageData : circleMapper.selectCirclecommentByMap(pd)) {
			comlist.add(
					new CircleComment(pageData.getAsString("comment_author_logo"), pageData.getAsString("comment_id"),
							pageData.getAsString("circle_id"), pageData.getAsString("comment_context_time"),
							pageData.getAsString("comment_context"), pageData.getAsString("user_id"),
							pageData.getAsString("p_id"), pageData.getAsString("comment_author")));
		}
		result.addAll(comlist);
		for (CircleComment firstComment : comlist) {
			// result.add(firstComment);
			recursionComment(result, firstComment);
		}
		try {
			Collections.sort(result, new Comparator<CircleComment>() {
				@Override
				public int compare(CircleComment o1, CircleComment o2) {
					if (EADate.getMillis(EADate.stringToDate(o1.getComment_context_time())) < EADate
							.getMillis(EADate.stringToDate(o2.getComment_context_time()))) {
						return 1;
					} else {
						return -1;
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (CircleComment pageData : result) {
			pageData.setComment_context_time(EADate.prettyTime(pageData.getComment_context_time()));
		}
		return result;
	}

	private void recursionComment(List<CircleComment> result, CircleComment comments) {
		PageData pdParam = new PageData();
		pdParam.put("p_id", comments.getComment_id());
		pdParam.put("circle_id", comments.getCircle_id());
		List<CircleComment> nextPageData = new ArrayList<CircleComment>();
		for (PageData pageData : circleMapper.selectCirclecommentByMap(pdParam)) {
			nextPageData.add(
					new CircleComment(pageData.getAsString("comment_author_logo"), pageData.getAsString("comment_id"),
							pageData.getAsString("circle_id"), pageData.getAsString("comment_context_time"),
							pageData.getAsString("comment_context"), pageData.getAsString("user_id"),
							pageData.getAsString("p_id"), pageData.getAsString("comment_author")));
		}
		if (nextPageData == null || nextPageData.size() == 0) {
			return;
		}
		result.addAll(nextPageData);
		for (CircleComment tempPd : nextPageData) {
			tempPd.setComment_author_alinas(tempPd.getComment_author() + "@" + comments.getComment_author());
			recursionComment(result, tempPd);
		}

	}

	/**
	 * 圈子列表（修改头像是用）
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PageData> circlelist(PageData pd) throws Exception {
		Page page = new Page();
		page.setPd(pd);
		return circleMapper.selectCircleByPage(page);
	}

	/**
	 * 圈子修改（修改头像是用）
	 * 
	 * @return
	 * @throws Exception
	 */
	public int updatacirclelist(PageData pd) throws Exception {
		return circleMapper.updateCircle(pd);
	}

	/**
	 * 圈子评论（修改头像是用）
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PageData> circlecommentlist(PageData pd) throws Exception {
		Page page = new Page();
		page.setPd(pd);
		return circleMapper.selectCirclecommentByPage(page);
	}

	/**
	 * 圈子评论修改（修改头像是用）
	 * 
	 * @return
	 * @throws Exception
	 */
	public int updateCirclecomment(PageData pd) throws Exception {
		return circleMapper.updateCirclecomment(pd);
	}
}