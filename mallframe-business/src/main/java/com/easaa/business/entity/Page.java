package com.easaa.business.entity;

import com.easaa.core.util.Tools;
import com.easaa.entity.PageData;

public class Page extends com.easaa.entity.Page{
	private int showCount=15; //每页显示记录数
	private int totalPage;		//总页数
	private int totalResult;	//总记录数
	private int currentPage;	//当前页
	private int currentResult;	//当前记录起始索引
	private boolean entityOrField;	//true:需要分页的地方，传入的参数就是Page实体；false:需要分页的地方，传入的参数所代表的实体拥有Page属性
	private boolean isApp;        //是否为app分页
	private String pageStr;		//最终页面显示的底部翻页导航，详细见：getPageStr();
	private PageData pd = new PageData();
	
	
	public Page(){
	}
	
	public int getTotalPage() {
		if(totalResult%showCount==0)
			totalPage = totalResult/showCount;
		else
			totalPage = totalResult/showCount+1;
		return totalPage;
	}
	
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	public int getTotalResult() {
		return totalResult;
	}
	
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}
	
	public int getCurrentPage() {
		if(currentPage<=0)
			currentPage = 1;
        if(!isApp){
        	if(currentPage>getTotalPage())
				currentPage = getTotalPage();
		}
		return currentPage;
	}
	
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	
	/**
	 * 
	 * <div class="pages">
        	<div class="pages1">
            	<ul>
                	<li> &lt; 上一页  </li>
                    <li>1</li>
                    <li>2</li>
                    <li>3</li>
                    <li>4</li>
                    <li>5</li>
                    <li>...</li>
                    <li> &gt;下一页</li>
                </ul>
                <div class="yeshu">共100页，</div>
                <div class="confirm">
                	<span>到第</span>
                    <input type="text" />
                    <span>页</span>
                    <button>确定</button>
                </div>
            </div>
        </div>
        
         <div class="col-sm-6">
                      </div>
                        <div class="col-sm-6 text-right sm-center">
                        <small class="inline table-options paging-info">共5页 &nbsp;&nbsp;72条数据</small>&nbsp;&nbsp;&nbsp;&nbsp;
                        <ul class="pagination pagination-xs nomargin pagination-custom">
                          <li class="disabled"><a href="#"><i class="fa fa-angle-double-left"></i></a></li>
                          <li class="active"><a href="#">1</a></li>
                          <li><a href="#">2</a></li>
                          <li><a href="#">3</a></li>
                          <li><a href="#">4</a></li>
                          <li><a href="#">5</a></li>
                          <li><a href="#"><i class="fa fa-angle-double-right"></i></a></li>
                        </ul>
                      </div>
        
	 */
	//拼接分页 页面及JS函数
		public String getPageStr() {
			StringBuffer sb = new StringBuffer();
			if(totalResult>0){
				sb.append("<div class='col-sm-6'></div><div class='col-sm-6'>\n <div >\n  <div class='col-sm-6 text-right sm-center'>");
				sb.append("<small class='inline table-options paging-info'>共"+totalPage+"页 &nbsp;&nbsp;"+totalResult+"条数据</small>&nbsp;&nbsp;&nbsp;&nbsp;");
				sb.append("<ul class='pagination pagination-xs nomargin pagination-custom'>");
				if(currentPage==1){
					sb.append("<li class='disabled'><a href='#'><i class='fa fa-angle-double-left'></i></a></li>");
				}else{
					sb.append("<li  onclick=\"nextPage("+(currentPage-1)+")\"><a href='#'><i class='fa fa-angle-double-left'></i></a></li>");
				}
				int showTag = 5;//分页标签显示数量
				int startTag = 1;
				int endTag = totalPage;
				if(currentPage>showTag){
					if(totalPage-currentPage < showTag){
						startTag = totalPage - showTag + 1;
						endTag = totalPage;
					}else{
						startTag = currentPage-1;
						endTag = startTag+showTag-1;
					}
				}else{
					if(currentPage!=1){
						startTag = currentPage-1;
					}
					endTag = startTag+showTag-1;
				}
				
				for(int i=startTag; i<=totalPage && i<=endTag; i++){
					if(currentPage==i)
						/*sb.append("<li class=\"active\" style='color:#FF7800;border:none;'><a><font>"+i+"</font></a></li>\n");*/
					    sb.append("<li class=\"active\"><a href='#'>"+i+"</a></li>\n");
					else
						/*sb.append("	<li onclick=\"nextPage("+i+")\"><a>"+i+"</a></li>\n");*/
					    sb.append("	<li onclick=\"nextPage("+i+")\"><a href='#'>"+i+"</a></li>\n");
				}
				if(currentPage==totalPage){
					sb.append("<li class='disabled'><a href='#'><i class='fa fa-angle-double-right'></i></a></li>");
				}else{
					sb.append("<li onclick=\"nextPage("+(currentPage+1)+")\"><a href='#'><i class='fa fa-angle-double-right'></i></a></li>");
				}
				sb.append("	 </ul></div>\n");
//				sb.append("<div class=\"confirm\">\n");
//				sb.append("<span>到第</span>\n");
//				sb.append("<input type=\"number\" value=\"\" id=\"toGoPage\"  placeholder=\"页码\"/>\n");
//				sb.append("<span>页</span>\n");
//				sb.append("<button onclick=\"toTZ();\">确定</button>\n");
//				sb.append("</div>\n");
//				sb.append("</div>\n");
				sb.append("<script type=\"text/javascript\">\n");
				//换页函数
				sb.append("function nextPage(page){");
				sb.append("	if(true && document.forms[0]){\n");
				sb.append("		var url = document.forms[0].getAttribute(\"action\");\n");
				sb.append("		if(url.indexOf('?')>-1){url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
				sb.append("		else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
				sb.append("		url = url + page + \"&" +(entityOrField?"showCount":"page.showCount")+"="+showCount+"\";\n");
				sb.append("		document.forms[0].action = url+\"&fid="+pd.getString("fid")+"\";\n");
				sb.append("		document.forms[0].submit();\n");
				sb.append("	}else{\n");
				sb.append("		var url = document.location+'';\n");
				sb.append("		url = url.substr(0,url.indexOf('?'));\n");
				sb.append("		if(url.indexOf('?')>-1){\n");
				sb.append("			if(url.indexOf('currentPage')>-1){\n");
				sb.append("				var reg = /currentPage=\\d*/g;\n");
				sb.append("				url = url.replace(reg,'currentPage=');\n");
				sb.append("			}else{\n");
				sb.append("				url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";\n");
				sb.append("			}\n");
				sb.append("		}else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
				sb.append("		url = url + page + \"&" +(entityOrField?"showCount":"page.showCount")+"="+showCount+"\";\n");
				sb.append("		document.location = url;\n");
				sb.append("	}\n");
				sb.append("}\n");
				
				//调整每页显示条数
				sb.append("function changeCount(value){");
				sb.append("	if(true && document.forms[0]){\n");
				sb.append("		var url = document.forms[0].getAttribute(\"action\");\n");
				sb.append("		if(url.indexOf('?')>-1){url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
				sb.append("		else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
				sb.append("		url = url + \"1&" +(entityOrField?"showCount":"page.showCount")+"=\"+value;\n");
				sb.append("		document.forms[0].action = url+\"&fid="+pd.getString("fid")+"\";\n");
				sb.append("		document.forms[0].submit();\n");
				sb.append("	}else{\n");
				sb.append("		var url = document.location+'';\n");
				sb.append("		url = url.substr(0,url.indexOf('?'));\n");
				sb.append("		if(url.indexOf('?')>-1){\n");
				sb.append("			if(url.indexOf('currentPage')>-1){\n");
				sb.append("				var reg = /currentPage=\\d*/g;\n");
				sb.append("				url = url.replace(reg,'currentPage=');\n");
				sb.append("			}else{\n");
				sb.append("				url += \"1&"+(entityOrField?"currentPage":"page.currentPage")+"=\";\n");
				sb.append("			}\n");
				sb.append("		}else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
				sb.append("		url = url + \"&" +(entityOrField?"showCount":"page.showCount")+"=\"+value;\n");
				sb.append("		document.location = url;\n");
				sb.append("	}\n");
				sb.append("}\n");
				
				//跳转函数 
				sb.append("function toTZ(){");
				sb.append("var toPaggeVlue = document.getElementById(\"toGoPage\").value;");
				sb.append("if(toPaggeVlue == ''){document.getElementById(\"toGoPage\").value=1;return;}");
				sb.append("if(isNaN(Number(toPaggeVlue))){document.getElementById(\"toGoPage\").value=1;return;}");
				sb.append("nextPage(toPaggeVlue);");
				sb.append("}\n");
				sb.append("</script>\n");
			}
			pageStr = sb.toString();
			return pageStr;
		}
		
	//拼接分页 页面及JS函数
//	public String getPageStr() {
//		StringBuffer sb = new StringBuffer();
//		if(totalResult>0){
//			sb.append("	<div class=\"pages\">\n<div class=\"pages1\">\n <ul>\n");
//			int showTag = 5;
//			int startTag = 1;
//			if(currentPage>showTag){
//				startTag = currentPage-1;
//			}
//			int endTag = startTag+showTag-1;
//			for(int i=startTag; i<=totalPage && i<=endTag; i++){
//				if(currentPage==i)
//					sb.append("<li class=\"active\"><a>"+i+"</a></li>\n");
//				else
//					sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage("+i+")\">"+i+"</a></li>\n");
//			}
//			if(currentPage==totalPage){
//				sb.append("	<li><a>下页</a></li>\n");
//			}else{
//				sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"nextPage("+(currentPage+1)+")\">下页</a></li>\n");
//			}
//			sb.append("</ul></div>");
//			sb.append("<script type=\"text/javascript\">\n");
//			sb.append("function nextPage(page){");
//			sb.append(" top.jzts();");
//			sb.append("	if(true && document.forms[0]){\n");
//			sb.append("		var url = document.forms[0].getAttribute(\"action\");\n");
//			sb.append("		if(url.indexOf('?')>-1){url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
//			sb.append("		else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
//			sb.append("		url = url + page + \"&" +(entityOrField?"showCount":"page.showCount")+"="+showCount+"\";\n");
//			sb.append("		document.forms[0].action = url+\"&fid="+pd.getString("fid")+"\";\n");
//			sb.append("		document.forms[0].submit();\n");
//			sb.append("	}else{\n");
//			sb.append("		var url = document.location+'';\n");
//			sb.append("		if(url.indexOf('?')>-1){\n");
//			sb.append("			if(url.indexOf('currentPage')>-1){\n");
//			sb.append("				var reg = /currentPage=\\d*/g;\n");
//			sb.append("				url = url.replace(reg,'currentPage=');\n");
//			sb.append("			}else{\n");
//			sb.append("				url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";\n");
//			sb.append("			}\n");
//			sb.append("		}else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
//			sb.append("		url = url + page + \"&" +(entityOrField?"showCount":"page.showCount")+"="+showCount+"\";\n");
//			sb.append("		document.location = url;\n");
//			sb.append("	}\n");
//			sb.append("}\n");
//			
//			//调整每页显示条数
//			sb.append("function changeCount(value){");
//			sb.append(" top.jzts();");
//			sb.append("	if(true && document.forms[0]){\n");
//			sb.append("		var url = document.forms[0].getAttribute(\"action\");\n");
//			sb.append("		if(url.indexOf('?')>-1){url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
//			sb.append("		else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
//			sb.append("		url = url + \"1&" +(entityOrField?"showCount":"page.showCount")+"=\"+value;\n");
//			sb.append("		document.forms[0].action = url+\"&fid="+pd.getString("fid")+"\";\n");
//			sb.append("		document.forms[0].submit();\n");
//			sb.append("	}else{\n");
//			sb.append("		var url = document.location+'';\n");
//			sb.append("		if(url.indexOf('?')>-1){\n");
//			sb.append("			if(url.indexOf('currentPage')>-1){\n");
//			sb.append("				var reg = /currentPage=\\d*/g;\n");
//			sb.append("				url = url.replace(reg,'currentPage=');\n");
//			sb.append("			}else{\n");
//			sb.append("				url += \"1&"+(entityOrField?"currentPage":"page.currentPage")+"=\";\n");
//			sb.append("			}\n");
//			sb.append("		}else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
//			sb.append("		url = url + \"&" +(entityOrField?"showCount":"page.showCount")+"=\"+value;\n");
//			sb.append("		document.location = url;\n");
//			sb.append("	}\n");
//			sb.append("}\n");
//			
//			//跳转函数 
//			sb.append("function toTZ(){");
//			sb.append("var toPaggeVlue = document.getElementById(\"toGoPage\").value;");
//			sb.append("if(toPaggeVlue == ''){document.getElementById(\"toGoPage\").value=1;return;}");
//			sb.append("if(isNaN(Number(toPaggeVlue))){document.getElementById(\"toGoPage\").value=1;return;}");
//			sb.append("nextPage(toPaggeVlue);");
//			sb.append("}\n");
//			sb.append("</script>\n");
//		}
//		pageStr = sb.toString();
//		return pageStr;
//	}
//	
	public void setPageStr(String pageStr) {
		this.pageStr = pageStr;
	}
	
	public int getShowCount() {
		return showCount;
	}
	
	public void setShowCount(int showCount) {
		
		this.showCount = showCount;
	}
	
	public int getCurrentResult() {
		currentResult = (getCurrentPage()-1)*getShowCount();
		if(currentResult<0)
			currentResult = 0;
		return currentResult;
	}
	
	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}
	
	public boolean isEntityOrField() {
		return entityOrField;
	}
	
	public void setEntityOrField(boolean entityOrField) {
		this.entityOrField = entityOrField;
	}
	public boolean isApp() {
		return isApp;
	}

	public void setApp(boolean isApp) {
		this.isApp = isApp;
	}

	public PageData getPd() {
		return pd;
	}

	public void setPd(PageData pd) {
		Tools.replaceEmpty(pd);
		this.pd = pd;
	}
}
