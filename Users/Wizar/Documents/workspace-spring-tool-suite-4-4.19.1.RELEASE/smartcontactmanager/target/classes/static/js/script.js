
const toggleSideBar = () => {
	if ($('.sidebar').is(":visible")) {
		$('.sidebar').css("display", "none");
		$('.content').css("margin-left", "0%");
	}
	else {
		$('.sidebar').css("display", "block");
		$('.content').css("margin-left", "20%");
	}
}
const payment = () => {
	let amount = $(".payment").val();
	if (amount == "" || amount == null) {
		alert("amount is required!....");
		return;
	}
	$.ajax({
		url: "/user/create_order",
		data: JSON.stringify({ amount: amount, info: "order_reques" }),
		contentType: "application/json",
		type: "POST",
		dataType: "json",
		success: function(response) {
			if (response.status == "created") {
				let options = {
					key: "rzp_test_EceT5bUJiygAbk",
					amount: response.amount,
					currency: "INR",
					name: "smart contacct manager",
					description: "PAYMENT",
					image: "https://www.thetimes.co.uk/imageserver/image/methode%2Ftimes%2Fprod%2Fweb%2Fbin%2Ffdefea84-949d-11e7-8177-dcdb1e4e95ab.jpg?crop=5584%2C3141%2C0%2C291",
					order_id: response.id,
					handler: function(response) {
						console.log(response.razorpay_payment_id);
						console.log(response.razorpay_order_id);
						console.log(response.razorpay_signature);
						alert("congrats!...");
					},
					"prefill": {
						"name": "",
						"email": "",
						"contact": ""
					},
					"notes": {
						"address": "Vipul Dongre"
					},
					"theme": {
						"color": "#3399cc"
					},
				};
				let rep = new Razorpay(options);
				rep.on('payment.failed', function(response) {
					alert(response.error.code);
					alert(response.error.description);
					alert(response.error.source);
					alert(response.error.step);
					alert(response.error.reason);
					alert(response.error.metadata.order_id);
					alert(response.error.metadata.payment_id);
				});
				rep.open();
			}
		},
		error: function(response) {
			console.log("error");
			console.log(response);

		},
	})
}