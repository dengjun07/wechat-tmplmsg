<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page isELIgnored="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="serverIp" value="${pageContext.request.serverName}" />
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>微信模板消息开发DEMO</title>
<link type="text/css" rel="stylesheet"
	href="${ctx}/resources/bootstrap/css/bootstrap.min.css" />
<link type="text/css" rel="stylesheet"
	href="${ctx}/resources/bootstrap/css/bootstrap-theme.min.css" />
<script type="text/javascript"
	src="${ctx}/resources/jquery-1.8.3.min.js"></script>
</head>
<body>
	<div class="container">
		<h2 class="page-header">微信模板消息开发DEMO.</h2>

		<div class="row">
			<div class="col-md-6">

				<form class="form-horizontal">
					<fieldset>
						<div id="legend" class="">
							<legend class="">微信预警发布</legend>
						</div>


						<div class="control-group">

							<!-- Text input-->
							<label class="control-label" for="input01">标题</label>
							<div class="controls">
								<input type="text" name="first" placeholder="如： 山西省气象局发布大雾黄色预警[III级/较大]" class="form-control" value="山西省气象局发布大雾黄色预警[III级/较大]"/>
							</div>
						</div>
						<div class="control-group">

							<!-- Text input-->
							<label class="control-label" for="input01">跳转网址</label>
							<div class="controls">
								<input type="text" name="url" placeholder="如：http://www.baidu.com" class="form-control" value="http://www.baidu.com">
							</div>
						</div>
						
						<div class="control-group">

							<!-- Text input-->
							<label class="control-label" for="input01">发布单位</label>
							<div class="controls">
								<input type="text" name="alarm_unit" placeholder="如：山西省气象局" class="form-control" value="山西省气象局">
							</div>
						</div>



						<div class="control-group">

							<!-- Text input-->
							<label class="control-label" for="input01">预警类别</label>
							<div class="controls">
								<input type="text" name="alarm_type" placeholder="如：大雾" class="form-control" value="大雾"/>
							</div>
						</div>

						<div class="control-group">

							<!-- Text input-->
							<label class="control-label" for="input01">预警级别</label>
							<div class="controls">
								<select class="form-control" name="alarm_level">
									<option value=[黄色预警/III级/较大]>[黄色预警/III级/较大]</option>
									<option value="[蓝色预警/IV级/一般]">[蓝色预警/IV级/一般]</option>
									<option value="[红色预警/I级/特别重大]">[红色预警/I级/特别重大]</option>
									<option value="[橙色预警/II级/重大]">[橙色预警/II级/重大]</option>
									<option value="[未知]">[未知]</option>
								</select>
							</div>
						</div>

						<div class="control-group">

							<!-- Textarea -->
							<label class="control-label">预警内容</label>
							<div class="controls">
								<div class="textarea">
									<textarea id="remark" type="" class="form-control"
										style="margin: 0px; width: 100%; height:120px;">山西省气象台2015年12月13日17时30分发布大雾黄色预警，预警区域：大同、朔州、忻州东部、太原、晋中、阳泉、长治、晋城。
									</textarea>
								</div>
							</div>
						</div>

						<div class="control-group">

							<!-- Select Basic -->
							<label class="control-label">使用字体颜色</label>
							<div class="controls">
								<select class="form-control" name="color">
									<option value="#ffd700">黄色</option>
									<option value="Blue">蓝色</option>
									<option value="Red">红色</option>
									<option value="#EE7621">橙色</option>
									<option value="grey">未知</option>
								</select>
							</div>

						</div>

						<div class="control-group">
							<label class="control-label"></label>

							<!-- Button -->
							<div class="controls">
								<button id="submit" class="btn btn-primary">开始发布</button>
							</div>
						</div>

					</fieldset>
				</form>


			</div>

			<div class="col-md-6">
				<form class="form-horizontal">
					<fieldset>
						<div id="legend" class="">
							<legend class="">微信服务器返回的消息</legend>
						</div>
						<div class="control-group">

							<!-- Textarea -->
							<label class="control-label"></label>
							<div class="controls">
								<div class="textarea">
									<textarea id="resultMsg" type="" class="form-control"
										style="margin: 0px; width: 100%; height: 500px; font-size: 20px; color: red;"> </textarea>
								</div>
							</div>
						</div>
					</fieldset>
				</form>

			</div>

		</div>

	</div>

	<script type="text/javascript">
		$('#submit').click(function() {
			var data = {
				url : $('input[name=url]').val(),
				color: $('select[name=color]').val(),
				first : $('input[name=first]').val(),
				alarm_unit : $('input[name=alarm_unit]').val(),
				alarm_type : $('input[name=alarm_type]').val(),
				alarm_level : $('select[name=alarm_level]').val(),
				remark : $('#remark').val()
			};

			$.ajax({
				url : '${ctx}/wx/sendTmplMsg.do',
				data : data,
				dataType : 'JSON',
				type : 'POST',
				success : function(result) {
					if (result.object) {
						$('#resultMsg').val(result.object);
					}
				}
			});

			return false;
		});
	</script>
</body>
</html>