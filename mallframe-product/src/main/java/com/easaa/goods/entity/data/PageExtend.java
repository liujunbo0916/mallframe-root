package com.easaa.goods.entity.data;

import java.util.List;

import com.easaa.entity.Page;
import com.easaa.entity.PageData;

public class PageExtend  extends Page{

	private List<PageData> resultPd;
	
	private int currentSize;
	
	//图片前缀
	private String prefixImg;
	

	
	/**
	 * 
	 * 
                <li><a class="page active" href="#">1</a></li>
                <li><a  class="page" href="#">2</a></li>
                <li><a  class="page" href="#">3</a></li>
                <li><a  class="page" href="#">4</a></li>
                <li><a  class="page" href="#">5</a></li>
                <li><a  class="page" href="#">6</a></li>
                <li><a  class="page" href="#">...</a></li>
                <li>
                    <a class="next" href="#" aria-label="Next">
                        下一页
                    </a>
                </li>
            </ul>
	 * 
	 */
	
	
	@Override
	public String getPageStr(){
		StringBuffer sb = new StringBuffer();
		if(getTotalResult()>0){
			sb.append("<ul class='pagination footer-pagination'>");
			if(getCurrentPage()==1){
				sb.append("	<li><a class=\"prev\" href=\"#\" aria-label=\"Previous\"> 上一页</a></li>\n");
			}else{
				sb.append("	<li onclick=\"nextPage("+(getCurrentPage()-1)+")\"><a class=\"prev\" href=\"#\" aria-label=\"Previous\">上页</a></li>\n");
			}
			int showTag = 5;//分页标签显示数量
			int startTag = 1;
			int endTag = getTotalPage();
			if(getCurrentPage()>showTag){
				if(getTotalPage()-getCurrentPage() < showTag){
					startTag = getTotalPage() - showTag + 1;
					endTag = getTotalPage();
				}else{
					startTag = getCurrentPage()-1;
					endTag = startTag+showTag-1;
				}
			}else{
				if(getCurrentPage()!=1){
					startTag = getCurrentPage()-1;
				}
				endTag = startTag+showTag-1;
			}
			
			for(int i=startTag; i<=getTotalPage() && i<=endTag; i++){
				if(getCurrentPage()==i)
					sb.append(" <li><a class=\"page active\" href=\"javascript:;\">"+i+"</a></li>\n");
				else
					sb.append("	<li onclick=\"nextPage("+i+")\"><a class=\"page\">"+i+"</a></li>\n");
			}
			if(getCurrentPage()==getTotalPage()){
				sb.append("	<li style=\"color:#C7C7C7;\"> <a class=\"next\" href=\"#\" aria-label=\"Next\">下一页</a></li>\n");
			}else{
				sb.append("	<li onclick=\"nextPage("+(getCurrentPage()+1)+")\"><a class=\"next\" href=\"#\" aria-label=\"Next\">下一页</a></li>\n");
			}
			sb.append("</ul>\n ");
//			sb.append("<div class=\"confirm\">\n");
//			sb.append("<span>到第</span>\n");
//			sb.append("<input type=\"number\" value=\"\" id=\"toGoPage\"  placeholder=\"页码\"/>\n");
//			sb.append("<span>页</span>\n");
//			sb.append("<button onclick=\"toTZ();\">确定</button>\n");
//			sb.append("</div>\n");
//			sb.append("</div>\n");
			sb.append("<script type=\"text/javascript\">\n");
			
			//换页函数
			sb.append("function nextPage(page){");
			sb.append("	if(true && document.forms[0]){\n");
			sb.append("		var url = document.forms[0].getAttribute(\"action\");\n");
			sb.append("		if(url.indexOf('?')>-1){url += \"&"+(isEntityOrField()?"currentPage":"page.currentPage")+"=\";}\n");
			sb.append("		else{url += \"?"+(isEntityOrField()?"currentPage":"page.currentPage")+"=\";}\n");
			sb.append("		url = url + page + \"&" +(isEntityOrField()?"showCount":"page.showCount")+"="+getShowCount()+"\";\n");
			sb.append("		document.forms[0].action = url+\"&fid="+getPd().getString("fid")+"\";\n");
			sb.append("		window.location.href=url;\n");
			sb.append("	}else{\n");
			sb.append("		var url = document.location+'';\n");
			sb.append("		url = url.substr(0,url.indexOf('?'));\n");
			sb.append("		if(url.indexOf('?')>-1){\n");
			sb.append("			if(url.indexOf('currentPage')>-1){\n");
			sb.append("				var reg = /currentPage=\\d*/g;\n");
			sb.append("				url = url.replace(reg,'currentPage=');\n");
			sb.append("			}else{\n");
			sb.append("				url += \"&"+(isEntityOrField()?"currentPage":"page.currentPage")+"=\";\n");
			sb.append("			}\n");
			sb.append("		}else{url += \"?"+(isEntityOrField()?"currentPage":"page.currentPage")+"=\";}\n");
			sb.append("		url = url + page + \"&" +(isEntityOrField()?"showCount":"page.showCount")+"="+getShowCount()+"\";\n");
			sb.append("		document.location = url;\n");
			sb.append("	}\n");
			sb.append("}\n");
			
			//调整每页显示条数
			sb.append("function changeCount(value){");
			sb.append("	if(true && document.forms[0]){\n");
			sb.append("		var url = document.forms[0].getAttribute(\"action\");\n");
			sb.append("		if(url.indexOf('?')>-1){url += \"&"+(isEntityOrField()?"currentPage":"page.currentPage")+"=\";}\n");
			sb.append("		else{url += \"?"+(isEntityOrField()?"currentPage":"page.currentPage")+"=\";}\n");
			sb.append("		url = url + \"1&" +(isEntityOrField()?"showCount":"page.showCount")+"=\"+value;\n");
			sb.append("		document.forms[0].action = url+\"&fid="+getPd().getString("fid")+"\";\n");
			sb.append("		document.forms[0].submit();\n");
			sb.append("	}else{\n");
			sb.append("		var url = document.location+'';\n");
			sb.append("		url = url.substr(0,url.indexOf('?'));\n");
			sb.append("		if(url.indexOf('?')>-1){\n");
			sb.append("			if(url.indexOf('currentPage')>-1){\n");
			sb.append("				var reg = /currentPage=\\d*/g;\n");
			sb.append("				url = url.replace(reg,'currentPage=');\n");
			sb.append("			}else{\n");
			sb.append("				url += \"1&"+(isEntityOrField()?"currentPage":"page.currentPage")+"=\";\n");
			sb.append("			}\n");
			sb.append("		}else{url += \"?"+(isEntityOrField()?"currentPage":"page.currentPage")+"=\";}\n");
			sb.append("		url = url + \"&" +(isEntityOrField()?"showCount":"page.showCount")+"=\"+value;\n");
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
		return sb.toString();
	}
	
	
	public int getCurrentSize() {
		return currentSize;
	}


	public void setCurrentSize(int currentSize) {
		if(currentSize > 0){
			currentSize = currentSize -1;
		}
		int eachNum = currentSize / 3;
		this.currentSize = eachNum;
	}


	public String getPrefixImg() {
		return prefixImg;
	}

	public void setPrefixImg(String prefixImg) {
		this.prefixImg = prefixImg;
	}

	public List<PageData> getResultPd() {
		return resultPd;
	}

	public void setResultPd(List<PageData> resultPd) {
		this.resultPd = resultPd;
	}


	
}
