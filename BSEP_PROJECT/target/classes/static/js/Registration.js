$(function(){
    let textfield = $("input[name=email]");
    let textfield2 = $("input[name = password]");
    let textfield3 = $("input[name = passwordConf]");
    let textfield4 = $("input[name = username]");

    $('button[type="submit"]').click(function(e) {
        e.preventDefault();

        if (textfield.val() != "" && textfield2.val() != "" && textfield4.val() != "" && textfield2.val()===textfield3.val()) {


            var registrationJSON = JSON.stringify({
                "email": textfield,
                "password": textfield2,
                "username": textfield4

            });

            console.log(registrationJSON);		// = println za javascript
            $.ajax({
                url: "http://localhost:8080/registration",		// full putanja do zeljene metode na backend-u
                method: "POST",
                contentType: "application/json",    	 //tip prosledjenog objekta
                data: registrationJSON,						// sta je prosledjeni objekat
                datatype: "text"
            });
        }
    });
});
