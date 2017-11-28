<?php
header('Content-type: application/json');
header("Content-type: text/html; charset=utf-8");

require_once ('../class_mysql.php');

$mysql 		= new Mysql('agent');
$mysql_game = new Mysql('ldyh');

$order_id = isset($_REQUEST['order_id'])?$_REQUEST['order_id']:'';
$sandbox = isset($_REQUEST['sandbox'])?intval($_REQUEST['sandbox']):1;
$apple_receipt = isset($_REQUEST['apple_receipt'])?$_REQUEST['apple_receipt']:'';//苹果内购的验证收据,由客户端传过来

$obj=$mysql->get_one('select count(*) as total from pay where order_id="'.$order_id.'"');

if(intval($obj['total'])==0||empty($apple_receipt))
{
    echo json_encode(array('code'=>-3));
	die();
}

$time =date("Y-m-d H:i:s", time());

if($sandbox==0)
{
	$url = 'https://sandbox.itunes.apple.com/verifyReceipt'; //测试验证地址
}
else
{
	$url = 'https://buy.itunes.apple.com/verifyReceipt';  //正式验证地址
}

$response = http_post_data($url,base64_decode($apple_receipt));

if($response['status'] == 0)
{
    $order_obj =$mysql->get_one('select * from pay where order_id="'.$order_id.'"');
	$uid  	= intval($order_obj['uid']);
	$result = intval($order_obj['result']);
	$money  = intval($order_obj['money']);
	$num  	= intval($order_obj['num']);
	$type  	= $order_obj['pay_type'];
	$card_type 	= $order_obj['card_type'];
	$game  		= $order_obj['game'];

	// 赠送数量
	$ext = chkSendNum($num);

	if($result==0)
	{

		$getback 	= $mysql->insert('pay_log',array("uid"=>$uid,"order_id"=>$order_id,"money"=>$money,"num"=>$num,"card_type"=>$card_type,"pay_type"=>$type,'game'=>$game,"time"=>$time));

		$mysql_game->insert(
							'charge',
							array(
								'orderId'  	=> $order_id,
								'username' 	=> $uid,
								'diamond' 	=> $num+$ext,
								'status' 	=> 0,
								'chargeTime'=> date('Y-m-d H:i:s',time())
							)
						);

			$url = 'http://120.76.208.38:8115/wolfKillLogin/functionServlet?type=chargeNotify&orderId='.$order_id;
			$ch = curl_init();  
			curl_setopt($ch,CURLOPT_URL,$url);  
			curl_setopt($ch,CURLOPT_HEADER,0);  
			curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1 );  
			curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 10);  
			$res = curl_exec($ch);  
			curl_close($ch);  
			$json_obj = json_decode($res,true);

			$mysql_log = new Mysql('log');
			$mysql_log->insert('user_paylog',array("Uid"=>$uid,"OrderId"=>$order_id,"Money"=>$money,"Num"=>$num,"SendNum"=>$ext,"CardType"=>$card_type,"PayType"=>$type,"Time"=>date("Y-m-d H:i:s",time()),'Game'=>$game));

			if($json_obj['code']==1)
			{
				echo json_encode(array('code'=>0));

				die();
			}else{
				echo json_encode(array('code'=>-3));

				die();
			}


		

	}else{

	    echo json_encode(array('code'=>-2));

		die();

	}

}else{

	//echo '验证失败'.$response->{'status'};

	$mysql->update('pay',array('result'=>1,'serverstate'=>$response['status'],'receipt'=>$apple_receipt,'identifer'=>$password,'datetime'=>$time),'order_id="'.$order_id.'"');

	echo json_encode(array('code'=>-1,'serverstate'=>$response['status']));

	die();
}


$urls = "http://120.76.208.38:8117/wolfKillLogin/functionServlet?type=chargeNotify&orderId=".$order_id;

http_post_data($urls,base64_decode($apple_receipt));

//curl请求苹果app_store验证地址

function http_post_data($url, $data_string) {

    $curl_handle=curl_init();

    curl_setopt($curl_handle,CURLOPT_URL, $url);

    curl_setopt($curl_handle,CURLOPT_RETURNTRANSFER, true);

    curl_setopt($curl_handle,CURLOPT_HEADER, 0);

    curl_setopt($curl_handle,CURLOPT_POST, true);

    curl_setopt($curl_handle,CURLOPT_POSTFIELDS, $data_string);

    curl_setopt($curl_handle,CURLOPT_SSL_VERIFYHOST, 0);

    curl_setopt($curl_handle,CURLOPT_SSL_VERIFYPEER, 0);

    $response_json =curl_exec($curl_handle);

    $response =json_decode($response_json,true);

    curl_close($curl_handle);

    return $response;

}

function chkSendNum($num)
{
	switch($num)
	{
		case 1796:
		$send_num=500;
		return $send_num;
		break;
		case 1176:
		$send_num=310;
		return $send_num;
		break;
		case 656:
		$send_num=160;
		return $send_num;
		break;
		case 276:
		$send_num=60;
		return $send_num;
		break;
		case 136:
		$send_num=25;
		return $send_num;
		break;
		case 60:
		$send_num=5;
		return $send_num;
		break;
		case 12:
		$send_num=0;
		return $send_num;
		break;
	}
}


?>