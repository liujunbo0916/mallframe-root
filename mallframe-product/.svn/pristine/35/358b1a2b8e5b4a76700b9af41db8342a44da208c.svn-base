package com.easaa.goods.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easaa.core.model.dao.EADao;
import com.easaa.core.model.service.impl.EABaseService;
import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.core.util.EAUtil;
import com.easaa.entity.Page;
import com.easaa.entity.PageData;
import com.easaa.goods.dao.GoodsCommentMapper;
import com.easaa.goods.entity.Comment;

@Service
public class GoodsCommentService extends EABaseService {
	@Autowired
	private GoodsCommentMapper goodsCommentMapper;
	@Override
	public EADao getDao() {
		// TODO Auto-generated method stub
		return goodsCommentMapper;
	}

	/**
	 * 商品/课程评价列表
	 * @param pd
	 * @return
	 */
	public List<Comment>  goodsCommentList(PageData pd) throws Exception {
		Page page = new Page();
		// 请求数据校验
		if (EAUtil.isEmpty(pd.getAsString("getcomment_id"))) {
			throw new Exception("商品、课程ID不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("comment_type"))) {
			throw new Exception("评价类型不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("currentPage"))) {
			throw new Exception("当前页不能为空");
		}
		//商品列表
		if(pd.getAsString("comment_type").equals("1")){
			pd.put("goods_id", pd.getAsString("getcomment_id"));
		}else if(pd.getAsString("comment_type").equals("2")){
			//课程列表
			pd.put("course_id", pd.getAsString("getcomment_id"));
		}else{
			throw new Exception("评价类型错误（1商品，2课程）");
		}
		pd.remove("getcomment_id");
		page.setApp(true);
		page.setCurrentPage(EAString.stringToInt(pd.getAsString("currentPage"), 1));
		page.setShowCount(15);
		page.setPd(pd);
		List<PageData> dataBasePd =  goodsCommentMapper.selectCommentListsPage(page);
		List<Comment> result = new ArrayList<>();
		Map<Comment,List<Comment>> commentMapChilds = new HashMap<Comment,List<Comment>>();
		Comment comment = null,replay = null;
		for(PageData tPd:dataBasePd){
			comment = new Comment(tPd.getAsString("comment_id"), tPd.getAsString("user_id"),tPd.getAsString("goods_id"), 
					tPd.getAsString("course_id"), tPd.getAsString("content"), tPd.getAsString("star"), 
					tPd.getAsString("comment_type"), tPd.getAsString("comment_img"), tPd.getAsString("create_time"),tPd.getAsString("nick_name"),tPd.getAsString("head_portrait"));
			replay = new Comment("", tPd.getAsString("p_user_id"), "", "", tPd.getAsString("p_content"),"", "", "","","","");
			
			if(!commentMapChilds.containsKey(comment)){
				commentMapChilds.put(comment, new ArrayList<Comment>());
			}
			commentMapChilds.get(comment).add(replay);
		}
		for(Entry<Comment,List<Comment>> entry:commentMapChilds.entrySet()){
			entry.getKey().setComments(entry.getValue());
			result.add(entry.getKey());
		}
		return result;
	}
	
	/**
	 * 商品课程评价不分页
	 */
	public List<PageData> commentList(PageData pd){
		
		String commentType = StringUtils.isNotEmpty(pd.getAsString("comment_type")) ? pd.getAsString("comment_type") : "1";
		pd.put("comment_type", commentType);
		if("1".equals(commentType)){
			pd.put("goods_id",pd.getAsString("target_id"));
		}else{
			pd.put("course_id",pd.getAsString("target_id"));
		}
		List<PageData> dataBasePd =  goodsCommentMapper.selectCommentList(pd);
		return dataBasePd;
	}
	/**
	 * 商品/课程评价
	 * @param pd
	 * @return
	 */
	public boolean setComment(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("userid不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("setcomment_id"))) {
			throw new Exception("商品/课程id不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("content"))) {
			throw new Exception("评语不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("star"))) {
			throw new Exception("评论星级不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("comment_type"))) {
			throw new Exception("评论类型不能为空（1商品，2课程）");
		}
		if (EAUtil.isEmpty(pd.getAsString("order_id"))) {
			throw new Exception("订单ID不能为空");
		}
		if(pd.getAsString("comment_type").equals("1")){
			pd.put("goods_id", pd.getAsString("setcomment_id"));
		}else if(pd.getAsString("comment_type").equals("2")){
			pd.put("course_id", pd.getAsString("setcomment_id"));
		}else{
			throw new Exception("评论类型错误（1商品，2课程）");
		}
		pd.remove("setcomment_id");
		Page page= new Page();
		page.setPd(pd);
		List<PageData> commentAlllist = goodsCommentMapper.selectByMap(page);
		if(commentAlllist!=null && commentAlllist.size()>0){
			throw new Exception("已评价过了，不要点了！");
		}
		pd.put("create_time", EADate.getCurrentTime());
		int index = create(pd);
		if (index > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 老师回复课程评价
	 * @param pd
	 * @return
	 */
	public boolean setCourseComment(PageData pd) throws Exception {
		if (EAUtil.isEmpty(pd.getAsString("user_id"))) {
			throw new Exception("userid不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("comment_id"))) {
			throw new Exception("课程评论ID不能为空");
		}
		if (EAUtil.isEmpty(pd.getAsString("content"))) {
			throw new Exception("评语不能为空");
		}
		pd.put("create_time", EADate.getCurrentTime());
		int index = create(pd);
		if (index > 0) {
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * 商品/课程评价列表(后台)
	 * @param pd
	 * @return
	 */
	public List<Comment>  goodsCommentList(Page page) throws Exception {
		List<PageData> dataBasePd =  goodsCommentMapper.selectCommentListsPage(page);
		List<Comment> result = new ArrayList<>();
		Map<Comment,List<Comment>> commentMapChilds = new HashMap<Comment,List<Comment>>();
		Comment comment = null,replay = null;
		for(PageData tPd:dataBasePd){
			comment = new Comment(tPd.getAsString("comment_id"), tPd.getAsString("user_id"),tPd.getAsString("goods_id"), 
					tPd.getAsString("course_id"), tPd.getAsString("content"), tPd.getAsString("star"), 
					tPd.getAsString("comment_type"), tPd.getAsString("comment_img"), tPd.getAsString("create_time"),tPd.getAsString("nick_name"),tPd.getAsString("head_portrait"));
			replay = new Comment("", tPd.getAsString("p_user_id"), "", "", tPd.getAsString("p_content"),"", "", "","","","");
			
			if(!commentMapChilds.containsKey(comment)){
				commentMapChilds.put(comment, new ArrayList<Comment>());
			}
			commentMapChilds.get(comment).add(replay);
		}
		for(Entry<Comment,List<Comment>> entry:commentMapChilds.entrySet()){
			entry.getKey().setComments(entry.getValue());
			result.add(entry.getKey());
		}
		return result;
	}
	
}
