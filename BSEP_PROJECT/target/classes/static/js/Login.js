$(function(){
    let textfield = $("input[name=user]");
    let texfield2 = $("input[name = password]")
    $('button[type="submit"]').click(function(e) {
        e.preventDefault();
        //little validation just to check username
        if (textfield.val() != "" && texfield2.val() != "") {
            //$("body").scrollTo("#output");

            var loginJSON = JSON.stringify({
                "username": textfield,
                "password": textfield2
            });

            console.log(loginJSON);		// = println za javascript
            $.ajax({
                url: "http://localhost:8080/login",		// full putanja do zeljene metode na backend-u
                method: "POST",
                contentType: "application/json",    	 //tip prosledjenog objekta
                data: loginJSON,						// sta je prosledjeni objekat
                datatype: "text"
            });
            $("#output").addClass("alert alert-success animated fadeInUp").html("Welcome back " + "<span style='text-transform:uppercase'>" + textfield.val() + "</span>");
            $("#output").removeClass(' alert-danger');
            $("input").css({
                "height":"0",
                "padding":"0",
                "margin":"0",
                "opacity":"0"
            });
            //change button text
            $('button[type="submit"]').html("continue")
                .removeClass("btn-info")
                .addClass("btn-default").click(function(){
                $("input").css({
                    "height":"auto",
                    "padding":"10px",
                    "opacity":"1"
                }).val("");
            });

            //show avatar
            $(".avatar").css({
                "background-image": "url('http://api.randomuser.me/0.3.2/portraits/women/35.jpg')"
            });
        } else {
            //remove success mesage replaced with error message
            $("#output").removeClass(' alert alert-success');
            $("#output").addClass("alert alert-danger animated fadeInUp").html("enter username and password");
        }
        //console.log(textfield.val());

    });
});
