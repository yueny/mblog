/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mtons.mblog.web.controller.admin;

import java.util.List;
import java.util.Set;

import com.mtons.mblog.base.lang.Result;
import com.mtons.mblog.bo.CommentBo;
import com.mtons.mblog.service.atom.bao.CommentService;
import com.mtons.mblog.service.manager.ICommentManagerService;
import com.mtons.mblog.web.controller.BaseBizController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author langhsu
 *
 */
@Controller("adminCommentController")
@RequestMapping("/admin/comment")
public class CommentController extends BaseBizController {
	@Autowired
	private ICommentManagerService commentManagerService;
	@Autowired
	private CommentService commentService;
	
	@RequestMapping("/list")
	public String list(ModelMap model) {
		Sort sort = Sort.by(
				new Sort.Order(Sort.Direction.DESC, "created")
		);

		Pageable pageable = wrapPageable(sort);
		Page<CommentBo> page = commentService.paging4Admin(pageable);
		model.put("page", page);
		return "/admin/comment/list";
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(@RequestParam("id") Set<Long> id) {
		Result data = Result.failure("操作失败");
		if (id != null) {
			try {
				commentManagerService.delete(id);
				data = Result.success();
			} catch (Exception e) {
				data = Result.failure(e.getMessage());
				logger.error("exception:", e);
			}
		}
		return data;
	}
}
