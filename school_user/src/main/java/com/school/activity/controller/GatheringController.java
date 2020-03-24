package com.school.activity.controller;

import com.school.activity.pojo.Gathering;
import com.school.activity.service.GatheringService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import util.FileUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/gathering")
public class GatheringController {

	@Autowired
	private GatheringService gatheringService;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(value = "/allGathing",method= RequestMethod.GET)
	public Result findAllGathing(){
		System.out.println("allGathing");
		return new Result(true,StatusCode.OK,"查询成功",gatheringService.findAll());
	}
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){

		return new Result(true,StatusCode.OK,"查询成功",gatheringService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",gatheringService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Gathering> pageList = gatheringService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Gathering>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",gatheringService.findSearch(searchMap));
    }

	/**
	 * 增加活动
	 * @param
	 */
	@RequestMapping(value="/addac/admin",method=RequestMethod.POST)
	public Result addActivity(@RequestParam("name") String name,
							  @RequestParam("detail") String detail,
							  @RequestParam("sponsor") String sponsor,
							  @RequestParam("starttime") String starttime,
							  @RequestParam("address") String address,
							  @RequestParam("phone") String phone,
							  @RequestParam("weixin") String weixin,
							  @RequestPart("uploadFile") MultipartFile uploadFile ){
		System.out.println("正在添加活动");
		System.out.println("name="+name);
//		//判断是否有权限访问
//		Claims claims=(Claims) request.getAttribute("admin_claims");
//		if(claims==null){
//			return new Result(true,StatusCode.ACCESSERROR,"无权访问");
//		}
		//上传的文件存放在一个绝对路径里
		String tempFileName = FileUtil.FileUpload(uploadFile,"D:\\imageschool\\adminimages\\","admin");

		// 保存
		Gathering gathering = new Gathering();
		gathering.setImage("/activityimage/"+tempFileName);
		gathering.setName(name);
		gathering.setAddress(address);
		gathering.setDetail(detail);
		gathering.setPhone(phone);
		gathering.setStarttime(starttime);
		gathering.setSponsor(sponsor);
		gathering.setWeixin(weixin);
		gatheringService.add(gathering);
		System.out.println("活动增加成功");
		return new Result(true,StatusCode.OK,"增加成功");
	}

	/**
	 * 增加
	 * @param gathering
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Gathering gathering  ){
		gatheringService.add(gathering);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param gathering
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Gathering gathering, @PathVariable String id ){
		gathering.setId(id);
		gatheringService.update(gathering);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		gatheringService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
