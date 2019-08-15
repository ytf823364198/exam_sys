package com.ziyue.controller;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ziyue.config.ShiroConfig;
import com.ziyue.entity.ExampleEnter;
import com.ziyue.entity.ExampleGoods;
import com.ziyue.service.ExampleEnterService;
import com.ziyue.service.ExampleGoodsService;
import com.ziyue.util.FileLoad;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;


@Controller
@Slf4j
@RequestMapping("/example/jasper")
@SuppressWarnings({ "unused", "unchecked", "rawtypes", "deprecation" })
public class ExampleJasperController  {
	@Autowired
	private ExampleGoodsService exampleGoodsService;
	@Autowired
	private ExampleEnterService exampleEnterService;
	@Autowired
	private DataSource dataSource;
		
	@RequestMapping("/index")
	public String bar() {
		return "/example/jasper/index";
	}
	
	
	/**
	 * 通过Map参数生产PDF文件
	 * @param response
	 */
	@RequestMapping("/MapParameter")
	public void mapParameter(HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("code", "123456");
			parameters.put("name", null);
			parameters.put("brand", "G470"); 
			parameters.put("model", "G470");
			parameters.put("quantity", 5);
			parameters.put("price", 3299d);
			parameters.put("total", 5*3299d);
			parameters.put("unit", "台");
			
			final String norm =	"1、锐角倒钝、去除毛刺飞边。\r\n" + 
					"　　2、零件去除氧化皮。\r\n" + 
					"　　3、未注圆角半径R5。\r\n" + 
					"　　4、未注倒角均为2×45°。\r\n" + 
					"　　5、未注形状公差应符合GB1184-80的要求。\r\n" + 
					"　　6、未注长度尺寸允许偏差±0.5mm。\r\n" + 
					"　　7、零件加工表面上，不应有划痕、擦伤等损伤零件表面的缺陷。\r\n" + 
					"　　8、精加工后的零件摆放时不得直接放在地面上，应采取必要的支撑、保护措施。加工面不允许有锈蛀和影响性能、寿命或外观的磕碰、划伤等缺陷。\r\n" + 
					"　　9、加工的螺纹表面不允许有黑皮、磕碰、乱扣和毛刺等缺陷。";
			parameters.put("norm", norm);
			
			
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/MapParameter.jasper");
			log.debug("resource.isOpen-->"+resource.isOpen());	
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, parameters,new JREmptyDataSource());
			servletOutputStream.flush();
			servletOutputStream.close(); 
			log.debug("resource.isOpen-->"+resource.isOpen());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
		
	/**
	 * JRBean填充商品信息
	 * @param response
	 */
	@RequestMapping("/JRBean")
	public void jrBean(HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			List<ExampleGoods> goodses = exampleGoodsService.findGoods(); 
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/JRBean.jasper");
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream,null,new JRBeanCollectionDataSource(goodses));
			servletOutputStream.flush();
			servletOutputStream.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 用List填充商品列表
	 * @param response
	 */
	@RequestMapping("/JRBeanList")
	public void jrBeanList( HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			Map<String,Object> parameters = new HashMap<String,Object>();
			List<ExampleGoods> goodses = exampleGoodsService.findGoods(); 
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/JRBeanList.jasper");
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, parameters,new JRBeanCollectionDataSource(goodses));
			servletOutputStream.flush();
			servletOutputStream.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 用JDBC填充商品信息
	 * @param response
	 */
	@RequestMapping("/JDBC")
	public void JDBC( HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");

			//Connection connection = dataSource.getConnection();
			Connection connection = DataSourceUtils.getConnection(dataSource);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/JDBC.jasper");
			
			String watermark = ClassUtils.getDefaultClassLoader().getResource("").getPath() +"/public/images/logo/zxing.jpg";
			Map  parameters = new HashMap();
			parameters.put("watermark",watermark);
			
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream,parameters ,connection);
			servletOutputStream.flush();
			servletOutputStream.close(); 
			//connection.close();
			//System.out.println(new Date());
			//System.out.println("connection-->"+connection.isClosed());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	
	/**
	 * 用JDBC填充商品列表
	 * @param response
	 */
	@RequestMapping("/JDBCList")
	public void JDBCList(HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			Connection connection = DataSourceUtils.getConnection(dataSource);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/JDBCList.jasper");
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, null,connection);
			servletOutputStream.flush();
			servletOutputStream.close(); 
			//connection.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
	}
	
	/**
	 * 用Parameters实现条件查询
	 * @param response
	 */
	@RequestMapping("/JDBCQuery")
	public void JDBCQuery(HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			Connection connection = DataSourceUtils.getConnection(dataSource);
			Map  parameters = new HashMap();
			parameters.put("code","10001" );
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/JDBCQuery.jasper");
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, parameters,connection);
			servletOutputStream.flush();
			servletOutputStream.close(); 
			//connection.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
	}
	
	/**
	 * 用Parameters实现IN,NOT IN查询; 格式为： $X{IN/NOTIN,Filed,parameter}
	 * @param response
	 */
	@RequestMapping("/JDBCInQuery")
	public void JDBCInQuery( HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			Connection connection = DataSourceUtils.getConnection(dataSource);
			
			Map  parameters = new HashMap();
			List <String>  codes = new ArrayList<String>() ;
			codes.add("10001");
			codes.add("10002");
			parameters.put("InQuery",codes );
			
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/JDBCInQuery.jasper");
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, parameters,connection);
			servletOutputStream.flush();
			servletOutputStream.close(); 
			//connection.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
	}
	
	@RequestMapping("/JDBCDynamicSQLQuery")
	public void JDBCDynamicSQLQuery( HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			Connection connection = DataSourceUtils.getConnection(dataSource);
			
			Map  parameters = new HashMap();
			parameters.put("JDBCDynamicSQLQuery"," where code in ('10001','10002') " );
			
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/JDBCDynamicSQLQuery.jasper");
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, parameters,connection);
			
			servletOutputStream.flush();
			servletOutputStream.close(); 
			//connection.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
	}
	
	
	/**
	 * 变量Variables的使用
	 * @param response
	 */
	@RequestMapping("/Variables")
	public void Variables( HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			Connection connection = DataSourceUtils.getConnection(dataSource);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/Variables.jasper");
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, null,connection);
			
			servletOutputStream.flush();
			servletOutputStream.close(); 
			//connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Images组件
	 * @param request
	 * @param response
	 */
	@RequestMapping("/Image")
	public void Image(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			
			final String IMAGE_URL ="http://localhost:"+ request.getServerPort()+ request.getContextPath()+"/example/jasper/images/xdxrgtq.jpg";
			//final String IMAGE_URL ="jasper/images/xdxrgtq.jpg";
			
			log.debug("IMAGE_URL-->"+ IMAGE_URL);
			Map parameters = new HashMap();
			parameters.put("IMAGE_URL", IMAGE_URL );

			//String IMAGE_IO = System.getProperty("user.dir") + "/src/main/resources/resources/example/jasper/images/xdxrgtq.jpg";
			String IMAGE_IO = ClassUtils.getDefaultClassLoader().getResource("").getPath() +"resources/example/jasper/images/xdxrgtq.jpg";
			parameters.put("IMAGE_IO", IMAGE_IO );
			log.debug("IMAGE_IO-->"+ IMAGE_IO);
			
			Resource Iresource = new ClassPathResource("resources/example/jasper/images/xdxrgtq.jpg");
			parameters.put("IMAGE_STREAM", Iresource.getInputStream() );
			
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/Images.jasper");
			
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, parameters,new JREmptyDataSource());
			
			servletOutputStream.flush(); 
			servletOutputStream.close(); 

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * 条码、二维码
	 * @param response
	 */
	@RequestMapping("/BarCode")
	public void BarCode(HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			Connection connection = DataSourceUtils.getConnection(dataSource);

			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/BarCode.jasper");
			
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, null,connection);
			servletOutputStream.flush(); 
			servletOutputStream.close(); 
			//connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 交叉报表
	 * @param response
	 */
	@RequestMapping("/Crossstab")
	public void Crossstab( HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			Connection connection = DataSourceUtils.getConnection(dataSource);
			
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/Crossstab.jasper");
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, null,connection);
			
			servletOutputStream.flush();
			servletOutputStream.close(); 
			//connection.close();
			//System.out.println(new Date());
			//System.out.println("connection-->"+connection.isClosed());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * 用向导创建分组报表
	 * @param response
	 */
	@RequestMapping("/Group")
	public void Group( HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			Connection connection = DataSourceUtils.getConnection(dataSource);
			
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/Group.jasper");
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, null,connection);
			
			servletOutputStream.flush();
			servletOutputStream.close(); 
			//connection.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
		
	/**
	 * 多栏的分组报表
	 * @param response
	 */
	@RequestMapping("/GroupColumn")
	public void GroupColumn(HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			Connection connection = DataSourceUtils.getConnection(dataSource);
			
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/GroupColumn.jasper");
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, null,connection);
			
			servletOutputStream.flush();
			servletOutputStream.close(); 
			//connection.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
		
	/**
	 * 分组报表的重置类型
	 * @param response
	 */
	@RequestMapping("/GroupResetType")
	public void ResetType(HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			Connection connection = DataSourceUtils.getConnection(dataSource);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/GroupResetType.jasper");
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, null,connection);
			
			servletOutputStream.flush();
			servletOutputStream.close(); 
			//connection.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * 自定义字体
	 * @param response
	 */
	@RequestMapping("/Font")
	public void Font(HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/Font.jasper");
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, null,new JREmptyDataSource());
			servletOutputStream.flush();
			servletOutputStream.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 继承、条件样式 
	 * @param response
	 */
	@RequestMapping("/Style")
	public void Style( HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			Connection connection = DataSourceUtils.getConnection(dataSource);
			
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/Style.jasper");
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, null,connection);
			servletOutputStream.flush();
			servletOutputStream.close(); 
			//connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * List控件的使用
	 * @param response
	 */
	@RequestMapping("/DataSetList")
	public void DataSetList(HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			
			Connection connection = DataSourceUtils.getConnection(dataSource);
			
			String id = "RK10001";
			Map parameters = new HashMap();
			parameters.put("id", id );
			
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/DataSetList.jasper");
			
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, parameters,connection);
			
			servletOutputStream.flush(); 
			servletOutputStream.close(); 
			//connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Table控件的使用
	 * @param response
	 */
	@RequestMapping("/DataSetTable")
	public void DataSetTable(HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			Connection connection = DataSourceUtils.getConnection(dataSource);
			
			String id = "RK10001";
			Map parameters = new HashMap();
			parameters.put("id", id );
			
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/DataSetTable.jasper");
			
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, parameters,connection);
			
			servletOutputStream.flush(); 
			servletOutputStream.close(); 
			//connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * JDBC数据源打印商品入库单及商品
	 * @param response
	 */
	@RequestMapping("/SubReportJDBC")
	public void SubReportJDBC( HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			Connection connection = DataSourceUtils.getConnection(dataSource);
			
			String SUBREPORT_DIR = ClassUtils.getDefaultClassLoader().getResource("").getPath() +"resources/example/jasper/";
			Map parameters = new HashMap();
			
			parameters.put("SUBREPORT_DIR", SUBREPORT_DIR );
			//parameters.put("id", "RK10001" );
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/SubReport_JDBC_Main.jasper");
			
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, parameters,connection);
			
			servletOutputStream.flush(); 
			servletOutputStream.close(); 
			//connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	

	/**
	 * 父子报表给子报表传递JRBean数据源
	 * @param response
	 */
	@RequestMapping("/SubReportJRBean")
	public void SubReportJRBean(HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			
			//log.debug("path-->" + Thread.currentThread().getContextClassLoader().getResource("/").getPath() );
			//String SUBREPORT_DIR = System.getProperty("user.dir") + "/src/main/resources/resources/example/jasper/";
			//log.debug("path-->" + ClassUtils.getDefaultClassLoader().getResource("").getPath() ); 
			//log.debug("ResourceUtils path-->" + ResourceUtils.getURL("classpath:").getPath() );
			
			String SUBREPORT_DIR = ClassUtils.getDefaultClassLoader().getResource("").getPath() +"resources/example/jasper/";
			log.debug("SUBREPORT_DIR ---> "+ SUBREPORT_DIR );
			
			List<ExampleEnter> enters = exampleEnterService.findEnters(); 
			
			for(ExampleEnter enter : enters){
				if( null != enter ){
					if(null != enter.getGoodses() && enter.getGoodses().size() < 8 ){
						for(int i = enter.getGoodses().size();i<8;i++){
							enter.getGoodses().add(new ExampleGoods());
						}
					}
				}
			}	
			
			Map parameters = new HashMap();
			parameters.put("SUBREPORT_DIR", SUBREPORT_DIR );
			
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/SubReport_JRBean_Main.jasper");
			
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, parameters,new JRBeanCollectionDataSource(enters));
			
			servletOutputStream.flush(); 
			servletOutputStream.close(); 

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 解决子报表不能分页套打的问题
	 * @param response
	 */
	@RequestMapping("/FixPageSizePrint")
	public void FixPageSizePrint(HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			Connection connection = DataSourceUtils.getConnection(dataSource);
			
			String enterId = "RK10003";
			ExampleEnter enter = exampleEnterService.findEnterById(enterId);
			int goodsCount = exampleEnterService.findEnterGoodsCountByEnterId(enterId);
			if(goodsCount % 8 != 0 ){
				goodsCount = goodsCount + 8-(goodsCount % 8);
			}
			//System.out.println("goodsCount-->"+goodsCount);
			
			Map parameters = new HashMap();
			parameters.put("enter", enter );
			parameters.put("enterid", enterId );
			parameters.put("goodsCount", goodsCount );
			
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/FixPageSizePrint.jasper");
			
			JasperRunManager.runReportToPdfStream(resource.getInputStream(),servletOutputStream, parameters,connection);
			
			servletOutputStream.flush(); 
			servletOutputStream.close(); 
			//connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 多报表合并
	 * @param response
	 */
	
	@RequestMapping("/FillReport")
	public void FillReport(HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=FillReport.pdf"); 
			Connection connection = DataSourceUtils.getConnection(dataSource);

				
				List<String> enterIds  = new ArrayList<String>();
				enterIds.add("RK10001");
				enterIds.add("RK10002");
				enterIds.add("RK10003");
				
				
				if(null != enterIds && enterIds.size() > 0){
					List<JasperPrint> printList = new ArrayList<JasperPrint>();
					for(String enterId : enterIds ){
						ServletOutputStream servletOutputStream = response.getOutputStream();
						Resource resource = new ClassPathResource("resources/example/jasper/FixPageSizePrint.jasper");

						ExampleEnter enter = exampleEnterService.findEnterById(enterId);
						int goodsCount = exampleEnterService.findEnterGoodsCountByEnterId(enterId);
						if(goodsCount % 8 != 0 ){
							goodsCount = goodsCount + 8-(goodsCount % 8);
						}

						Map parameters = new HashMap();
						parameters.put("enter", enter );
						parameters.put("enterid", enterId );
						parameters.put("goodsCount", goodsCount );
						
						JasperPrint print = JasperFillManager.fillReport(resource.getInputStream(), parameters, connection);
						printList.add(print);
					}
					ServletOutputStream servletOutputStream = response.getOutputStream();
					JRExporter  exporter = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
					exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,printList);
					exporter.exportReport();
					servletOutputStream.flush();
					servletOutputStream.close(); 
					
				}
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			} 				
	}	

	
	/**
	 * 用JDBC填充商品列表
	 * @param response
	 */
	@RequestMapping("/Excel")
	public void Excel(HttpServletRequest request,HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			//response.setContentType("application/vnd.ms-excel");  
			response.setContentType("application/x-xls"); 
		    response.setHeader("Content-disposition", "attachment; filename=" +FileLoad.encodeFileName(request,"Excel中文.xlsx") ); 
		    
			Connection connection = DataSourceUtils.getConnection(dataSource);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			Resource resource = new ClassPathResource("resources/example/jasper/Excel.jasper");

			List<JasperPrint> printList = new ArrayList<JasperPrint>();
			JasperPrint print =  JasperFillManager.fillReport(resource.getInputStream(), null,connection);
			printList.add(print);
			//RDocxExporter exporter=new JRDocxExporter();
			JRExporter  exporter = new JRXlsxExporter();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,printList);
			exporter.exportReport();
			servletOutputStream.flush();
			servletOutputStream.close(); 
			printList.clear();
			
			//connection.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
	}
	
	
}