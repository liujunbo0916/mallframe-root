package com.easaa.content.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easaa.content.dao.ContentCommentMapper;
import com.easaa.content.dao.ContentMapper;
import com.easaa.content.entity.ContentComment;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EAUtil;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;

@Service
public class ContentService extends EABaseService {

	@Autowired
	private ContentMapper contentMapper;

	@Autowired
	private ContentCommentMapper contentcommentMapper;

	@Override
	public EADao getDao() {
		return contentMapper;
	}

	/**
	 * 文章列表
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> selectContentlist(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("currentPage"))) {
			throw new Exception("页数不能为空！");
		}
		pd.put("orderby", "0");
		Page page = new Page();
		page.setApp(true);
		page.setCurrentPage(Integer.parseInt(pd.getAsString("currentPage")));
		page.setShowCount(15);
		page.setPd(pd);
		List<PageData> data = contentMapper.selectlistByPage(page);
		for (PageData pageData : data) {
			PageData upd = new PageData();
			upd.put("user_id", pd.getAsString("user_id"));
			upd.put("content_id", pageData.getAsString("CONTENT_ID"));
			pageData.put("IS_LIKED", selectThumbsupById(upd));
		}
		return data;
	}

	/**
	 * 查询文章是否点赞
	 * 
	 * @param upd
	 * @return
	 */
	public int selectThumbsupById(PageData upd) {
		PageData udata = contentcommentMapper.selectThumbsupById(upd);
		if (EAUtil.isNotEmpty(udata)) {
			return 1;
		}
		return 0;
	}

	/**
	 * 文章推荐(首页)
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> recommendContentlist(PageData pd) throws Exception {
		pd.put("orderby", 1);
		pd.put("limit", 6);
		List<PageData> data = contentMapper.selectHomelist(pd);
		return data;
	}
	
	/**
	 * 老师关联文章 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> teaContentlist(String id) throws Exception {
		PageData pd = new PageData();
		pd.put("orderby", 1);
		pd.put("limit", 6);
		pd.put("PUBLISHER_ID",id);
		List<PageData> data = contentMapper.selectHomelist(pd);
		return data;
	}
	/**
	 * 文章推荐(文章详情)
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> recommendContent(String code) throws Exception {
		PageData pd = new PageData();
		pd.put("CATEGORY_CODE", code);
		pd.put("orderby", 1);
		pd.put("limit", 5);
		List<PageData> data = contentMapper.selectHomelist(pd);
		return data;
	}

	/**
	 * 文章详情
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData contentDetails(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("CONTENT_ID"))) {
			throw new Exception("CONTENT_ID不能为空！");
		}
		PageData data = contentMapper.selectDetails(pd);
		return data;
	}

	/**
	 * 文章评论回复
	 * 
	 * @return (content_id,p_id,comment_context) p_id 评论0 回复 comment_id
	 *         content_id 文章id comment_context 评论回复内容
	 * @throws Exception
	 */
	public PageData contentComment(PageData pd) throws Exception {
		PageData data = new PageData();
		int cct = contentcommentMapper.insert(pd);
		if (cct > 0) {
			Page page = new Page();
			data.put("p_id", "0");
			data.put("content_id", pd.getAsString("content_id"));
			page.setPd(data);
			List<PageData> list = contentcommentMapper.selectByMap(page);
			data.remove("p_id");
			data.remove("content_id");
			data.put("comment_num", list.size());
			return data;
		}
		return data;
	}

	/**
	 * 文章点赞
	 * 
	 * @return
	 * @throws Exception
	 */
	public PageData givethumbsUp(PageData pd) throws Exception {
		PageData data = new PageData();
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("user_id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("content_id"))) {
			throw new Exception("circle_id不能为空");
		}
		PageData tup = contentcommentMapper.selectThumbsupById(pd);
		if (EAUtil.isEmpty(tup)) {
			int cct = contentcommentMapper.insertThumbsup(pd);
			if (cct > 0) {
				data.put("content_id", pd.getAsString("content_id"));
				List<PageData> list = contentcommentMapper.selectThumbsuplist(data);
				contentcommentMapper.selectThumbsupById(pd);
				data.remove("content_id");
				data.put("thumbsUp_num", list.size());
				return data;
			}
		} else {
			throw new Exception("你已经点过赞了");
		}
		return data;
	}

	/**
	 * 文章评论列表
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<ContentComment> contentCommentlist(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("content_id"))) {
			throw new Exception("content_id不能为空");
		}
		List<ContentComment> list = commentList(pd);
		return list;
	}

	/**
	 * 递归 评论列表
	 * 
	 * @param pd
	 * @return
	 */
	public List<ContentComment> commentList(PageData pd) {
		Page page = new Page();
		pd.put("p_id", "0");
		page.setPd(pd);
		List<ContentComment> comlist = new ArrayList<ContentComment>();
		for (PageData pageData : contentcommentMapper.selectByMap(page)) {
			comlist.add(
					new ContentComment(pageData.getAsString("comment_author_logo"), pageData.getAsString("comment_id"),
							pageData.getAsString("content_id"), pageData.getAsString("comment_context_time"),
							pageData.getAsString("comment_context"), pageData.getAsString("user_id"),
							pageData.getAsString("p_id"), pageData.getAsString("comment_author")));
		}
		List<ContentComment> result = new ArrayList<ContentComment>();
		result.addAll(comlist);
		for (ContentComment firstComment : comlist) {
			// result.add(firstComment);
			recursionComment(result, firstComment);
		}
		try {
			Collections.sort(result, new Comparator<ContentComment>() {
				@Override
				public int compare(ContentComment o1, ContentComment o2) {
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
		for (ContentComment pageData : result) {
			pageData.setComment_context_time(EADate.prettyTime(pageData.getComment_context_time()));
		}
		return result;
	}

	private void recursionComment(List<ContentComment> result, ContentComment comments) {
		PageData pdParam = new PageData();
		pdParam.put("p_id", comments.getComment_id());
		pdParam.put("content_id", comments.getContent_id());
		List<ContentComment> nextPageData = new ArrayList<ContentComment>();
		Page page = new Page();
		page.setPd(pdParam);
		for (PageData pageData : contentcommentMapper.selectByMap(page)) {
			nextPageData.add(
					new ContentComment(pageData.getAsString("comment_author_logo"), pageData.getAsString("comment_id"),
							pageData.getAsString("content_id"), pageData.getAsString("comment_context_time"),
							pageData.getAsString("comment_context"), pageData.getAsString("user_id"),
							pageData.getAsString("p_id"), pageData.getAsString("comment_author")));
		}
		if (nextPageData == null || nextPageData.size() == 0) {
			return;
		}
		result.addAll(nextPageData);
		for (ContentComment tempPd : nextPageData) {
			tempPd.setComment_author_alinas(tempPd.getComment_author() + "@" + comments.getComment_author());
			recursionComment(result, tempPd);
		}

	}

	public PageData selMaxLv(PageData pd) {
		List<PageData> contents = contentMapper.selMaxLv(pd);
		return (contents == null || contents.size() == 0) ? null : contents.get(0);
	}

	/**
	 * 文章评论回复列表（修改头像时调用）
	 * 
	 * @throws Exception
	 */
	public List<PageData> contentComments(PageData pd) throws Exception {
		Page page = new Page();
		page.setPd(pd);
		return contentcommentMapper.selectByMap(page);
	}

	/**
	 * 文章评论回复修改（修改头像时调用）
	 * 
	 * @throws Exception
	 */
	public int updateComment(PageData pd) throws Exception {
		return contentcommentMapper.update(pd);
	}
	
	/**
	 * 删除图片
	 */
	public void deleteImg(PageData pd){
		contentMapper.deleteImg(pd);
	}
	
	
	
	/**
	 * #################涠洲岛新闻####################################
	 */
	/**
	 * 查询固定新闻 如果固定新闻没有或者少于4条 查焦点新闻 补上去   
	 * 如果焦点新闻和固定新闻也不满4条 查普通新闻
	 * 参数 limit 限制条数
	 */
	public PageData topNewsLimit(PageData pd){
		PageData result = new PageData();
		if(StringUtils.isEmpty(pd.getAsString("limit")) || "0".equalsIgnoreCase(pd.getAsString("limit")) ){
			pd.put("limit", 4);
		}
		List<PageData> tops = contentMapper.selectTopLimit(pd);
		if(EAUtil.isEmpty(tops)){ //如果固定没有值 查询 焦点
			tops = contentMapper.selectFoucsLimit(pd);
		}
		if(EAUtil.isEmpty(tops)){
			tops = contentMapper.selectByPutime(pd);
		}
		if(!EAUtil.isEmpty(tops)){ //获取列表不需要查出来的新闻
			StringBuffer sb = new StringBuffer();
			for(PageData tpd : tops){
				sb.append(tpd.getAsString("CONTENT_ID")+",");
			}
			result.put("exclude", sb.substring(0, sb.length()-1).toString());
			result.put("tops", tops);
		}
		return result;
	}
	/**
	 * 涠洲岛 项目新闻列表 (分页)
	 * 显示方式 ：顶部显示固定新闻最多4个，下方新闻列表
	 * currentPage   当前页
	 * exclude    排除新闻   逗号分割
	 * isThumb 是否开启 点赞校验
	 */
	public List<PageData> newsListPage(PageData pd,boolean isThumb){
		Page page = new Page();
		page.setApp(true);
		page.setCurrentPage(EAString.stringToInt(pd.getAsString("currentPage"), 1));
		page.setShowCount(10);
		page.setPd(pd);
		List<PageData> news =  contentMapper.selectByPutimePage(page);
		
		for (PageData pageData : news) {
			if("1".equals(pageData.getAsString("MODEL_TYPE"))){
				pageData.put("albums", selectContentAlbums(pageData.getAsInt("CONTENT_ID")));
			}
		}
		
		if(isThumb){
			for(PageData tpd:news){
				tpd.put("user_id", pd.getAsString("user_id"));
				tpd.put("content_id", tpd.getAsString("CONTENT_ID"));
				List<PageData> thumbsups = contentMapper.selectThumbsup(tpd);
				if(thumbsups != null && thumbsups.size() > 0 ){
					tpd.put("thumb", 1);
				}else{
					tpd.put("thumb", "0");
				}
			}
		}
		return news;
		
	}
	
	public List<PageData> selectContentAlbums(Integer contentId){
		return contentMapper.selectContentAlbums(contentId);
	}
	
	
	public List<PageData> selectByPutimePage(Page page){
		return contentMapper.selectByPutimePage(page);
	}
	public List<PageData> selectByPutime(PageData pd){
		return contentMapper.selectByPutime(pd);
	}
	/**
	 * 都匀WX专用
	 * @param page
	 * @return
	 */
	public List<PageData> selectAllByPage(Page page){
		List<PageData> contents = new ArrayList<PageData>();
		if(EAUtil.isEmpty(page.getPd().getAsString("CATEGORY_CODE"))){
			PageData pd= new PageData();
			pd.put("limit", 1); // 先限制成1条
			PageData result = topNewsLimit(pd);
			if (result != null) {
				contents.add(result.getAsList("tops").get(0));
			}
		}
		List<PageData> dataList = contentMapper.selectAllByPage(page);
		contents.addAll(dataList);
		Set set = new HashSet();
		List<PageData> newList = new ArrayList<PageData>();
		for (PageData cd : contents) {
			if (set.add(cd)) {
				newList.add(cd);
			}
		}
		contents.clear();
		contents = newList;
		for (PageData ct : contents) {
			/**
			 * 查询相册
			 */
			if ("1".equals(ct.getAsString("MODEL_TYPE"))) {
				ct.put("albums", selectContentAlbums(ct.getAsInt("CONTENT_ID")));
			}
			Page compage= new Page();
			PageData data= new PageData();
			data.put("content_id", ct.getAsString("CONTENT_ID"));
			compage.setPd(data);
			List<PageData> comments = contentcommentMapper.selectByMap(compage);
			ct.put("comments", comments.size());
			List<PageData> list = contentcommentMapper.selectThumbsuplist(data);
			ct.put("thumbsups", list.size());
		}
		return contents;
	}
	
	/**
	 * 都匀WX资讯
	 * 
	 * 查询固定新闻 如果固定新闻没有或者少于4条 查焦点新闻 补上去   
	 * 如果焦点新闻和固定新闻也不满4条 查普通新闻
	 * 参数 limit 限制条数
	 */
	public List<PageData> topLimit(PageData pd){
		List<PageData> tops = contentMapper.selectTopLimit(pd);
		if(EAUtil.isEmpty(tops)){ //如果固定没有值 查询 焦点
			tops = contentMapper.selectFoucsLimit(pd);
		}
		if(EAUtil.isEmpty(tops)){
			tops = contentMapper.selectByPutime(pd);
		}
		if(EAUtil.isEmpty(tops)){
			Page page= new Page();
			page.setPd(pd);
			page.setShowCount(pd.getAsInt("limit"));
			tops=contentMapper.selectAllByPage(page);
		}
		for (PageData pageData : tops) {
			Page compage= new Page();
			PageData data= new PageData();
			data.put("content_id", pageData.getAsString("CONTENT_ID"));
			compage.setPd(data);
			pageData.put("comments", contentcommentMapper.selectByMap(compage).size());
			pageData.put("thumbsups", contentcommentMapper.selectThumbsuplist(data).size());
		}
		return tops;
	}
	public void insertContentAlbums(PageData pd){
		contentMapper.insertContentAlbums(pd);
	}
	public void updateContentAlbums(PageData pd){
		contentMapper.updateContentAlbums(pd);
	}
	public void deleteContentAlbums(PageData pd){
		contentMapper.deleteContentAlbums(pd);
	}

	public List<PageData> selectHotContent(PageData pd) {
		return contentMapper.selectHotContent(pd);
	}

	public PageData selectPrevOrNext(PageData pd) {
		return contentMapper.selectPrevOrNext(pd);
	}
}
