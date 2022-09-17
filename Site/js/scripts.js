/***************** Waypoints ******************/

$(document).ready(function() {

	$('.wp1').waypoint(function() {
		$('.wp1').addClass('animated fadeInLeft');
	}, {
		offset: '75%'
	});
	$('.wp2').waypoint(function() {
		$('.wp2').addClass('animated fadeInUp');
	}, {
		offset: '75%'
	});
	$('.wp3').waypoint(function() {
		$('.wp3').addClass('animated fadeInDown');
	}, {
		offset: '55%'
	});
	$('.wp4').waypoint(function() {
		$('.wp4').addClass('animated fadeInDown');
	}, {
		offset: '75%'
	});
	$('.wp5').waypoint(function() {
		$('.wp5').addClass('animated fadeInUp');
	}, {
		offset: '75%'
	});
	$('.wp6').waypoint(function() {
		$('.wp6').addClass('animated fadeInDown');
	}, {
		offset: '75%'
	});
});

/***************** Slide-In Nav ******************/

$(window).load(function() {

	$('.nav_slide_button').click(function() {
		$('.pull').slideToggle();
	});

});

/***************** Smooth Scrolling ******************/

$(function() {

	$('a[href*=#]:not([href=#])').click(function() {
		if (location.pathname.replace(/^\//, '') === this.pathname.replace(/^\//, '') && location.hostname === this.hostname) {

			var target = $(this.hash);
			target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
			if (target.length) {
				$('html,body').animate({
					scrollTop: target.offset().top
				}, 2000);
				return false;
			}
		}
	});

});

/***************** Nav Transformicon ******************/

document.querySelector("#nav-toggle").addEventListener("click", function() {
	this.classList.toggle("active");
});

/***************** Overlays ******************/

$(document).ready(function(){
    if (Modernizr.touch) {
        // show the close overlay button
        $(".close-overlay").removeClass("hidden");
        // handle the adding of hover class when clicked
        $(".img").click(function(e){
            if (!$(this).hasClass("hover")) {
                $(this).addClass("hover");
            }
        });
        // handle the closing of the overlay
        $(".close-overlay").click(function(e){
            e.preventDefault();
            e.stopPropagation();
            if ($(this).closest(".img").hasClass("hover")) {
                $(this).closest(".img").removeClass("hover");
            }
        });
    } else {
        // handle the mouseenter functionality
        $(".img").mouseenter(function(){
            $(this).addClass("hover");
        })
        // handle the mouseleave functionality
        .mouseleave(function(){
            $(this).removeClass("hover");
        });
    }
});

function getBookmarklet(bool) {
	prompt("Создайте закладку, вставив вместо адреса строку ниже. Внимайтельно проверьте начало и конец вставленной строки (некоторые браузеры убирают 'javascript:' из начала строки при копировании). Будьте осторожны! Не допускайте попадания закладки в чужие руки!", "javascript:void%20function(){function%20e(e){if(null===e||%22undefined%22==typeof%20e)return%22%22;e+=%22%22;var%20r,t,a=%22%22,n=0;r=t=0;for(var%20n=e.length,o=0;n%3Eo;o++){var%20i=e.charCodeAt(o),s=null;if(128%3Ei)t++;else%20if(i%3E127%26%262048%3Ei)s=String.fromCharCode(i%3E%3E6|192,63%26i|128);else%20if(55296!=(63488%26i))s=String.fromCharCode(i%3E%3E12|224,i%3E%3E6%2663|128,63%26i|128);else{if(55296!=(64512%26i))throw%20new%20RangeError(%22Unmatched%20trail%20surrogate%20at%20%22+o);if(s=e.charCodeAt(++o),56320!=(64512%26s))throw%20new%20RangeError(%22Unmatched%20lead%20surrogate%20at%20%22+(o-1));i=((1023%26i)%3C%3C10)+(1023%26s)+65536,s=String.fromCharCode(i%3E%3E18|240,i%3E%3E12%2663|128,i%3E%3E6%2663|128,63%26i|128)}null!==s%26%26(t%3Er%26%26(a+=e.slice(r,t)),a+=s,r=t=o+1)}return%20t%3Er%26%26(a+=e.slice(r,n)),a}function%20r(e,r){var%20i=e[0],s=e[1],l=e[2],f=e[3],i=a(i,s,l,f,r[0],7,-680876936),f=a(f,i,s,l,r[1],12,-389564586),l=a(l,f,i,s,r[2],17,606105819),s=a(s,l,f,i,r[3],22,-1044525330),i=a(i,s,l,f,r[4],7,-176418897),f=a(f,i,s,l,r[5],12,1200080426),l=a(l,f,i,s,r[6],17,-1473231341),s=a(s,l,f,i,r[7],22,-45705983),i=a(i,s,l,f,r[8],7,1770035416),f=a(f,i,s,l,r[9],12,-1958414417),l=a(l,f,i,s,r[10],17,-42063),s=a(s,l,f,i,r[11],22,-1990404162),i=a(i,s,l,f,r[12],7,1804603682),f=a(f,i,s,l,r[13],12,-40341101),l=a(l,f,i,s,r[14],17,-1502002290),s=a(s,l,f,i,r[15],22,1236535329),i=n(i,s,l,f,r[1],5,-165796510),f=n(f,i,s,l,r[6],9,-1069501632),l=n(l,f,i,s,r[11],14,643717713),s=n(s,l,f,i,r[0],20,-373897302),i=n(i,s,l,f,r[5],5,-701558691),f=n(f,i,s,l,r[10],9,38016083),l=n(l,f,i,s,r[15],14,-660478335),s=n(s,l,f,i,r[4],20,-405537848),i=n(i,s,l,f,r[9],5,568446438),f=n(f,i,s,l,r[14],9,-1019803690),l=n(l,f,i,s,r[3],14,-187363961),s=n(s,l,f,i,r[8],20,1163531501),i=n(i,s,l,f,r[13],5,-1444681467),f=n(f,i,s,l,r[2],9,-51403784),l=n(l,f,i,s,r[7],14,1735328473),s=n(s,l,f,i,r[12],20,-1926607734),i=t(s^l^f,i,s,r[5],4,-378558),f=t(i^s^l,f,i,r[8],11,-2022574463),l=t(f^i^s,l,f,r[11],16,1839030562),s=t(l^f^i,s,l,r[14],23,-35309556),i=t(s^l^f,i,s,r[1],4,-1530992060),f=t(i^s^l,f,i,r[4],11,1272893353),l=t(f^i^s,l,f,r[7],16,-155497632),s=t(l^f^i,s,l,r[10],23,-1094730640),i=t(s^l^f,i,s,r[13],4,681279174),f=t(i^s^l,f,i,r[0],11,-358537222),l=t(f^i^s,l,f,r[3],16,-722521979),s=t(l^f^i,s,l,r[6],23,76029189),i=t(s^l^f,i,s,r[9],4,-640364487),f=t(i^s^l,f,i,r[12],11,-421815835),l=t(f^i^s,l,f,r[15],16,530742520),s=t(l^f^i,s,l,r[2],23,-995338651),i=o(i,s,l,f,r[0],6,-198630844),f=o(f,i,s,l,r[7],10,1126891415),l=o(l,f,i,s,r[14],15,-1416354905),s=o(s,l,f,i,r[5],21,-57434055),i=o(i,s,l,f,r[12],6,1700485571),f=o(f,i,s,l,r[3],10,-1894986606),l=o(l,f,i,s,r[10],15,-1051523),s=o(s,l,f,i,r[1],21,-2054922799),i=o(i,s,l,f,r[8],6,1873313359),f=o(f,i,s,l,r[15],10,-30611744),l=o(l,f,i,s,r[6],15,-1560198380),s=o(s,l,f,i,r[13],21,1309151649),i=o(i,s,l,f,r[4],6,-145523070),f=o(f,i,s,l,r[11],10,-1120210379),l=o(l,f,i,s,r[2],15,718787259),s=o(s,l,f,i,r[9],21,-343485551);e[0]=c(i,e[0]),e[1]=c(s,e[1]),e[2]=c(l,e[2]),e[3]=c(f,e[3])}function%20t(e,r,t,a,n,o){return%20r=c(c(r,e),c(a,o)),c(r%3C%3Cn|r%3E%3E%3E32-n,t)}function%20a(e,r,a,n,o,i,s){return%20t(r%26a|~r%26n,e,r,o,i,s)}function%20n(e,r,a,n,o,i,s){return%20t(r%26n|a%26~n,e,r,o,i,s)}function%20o(e,r,a,n,o,i,s){return%20t(a^(r|~n),e,r,o,i,s)}function%20i(e){txt=%22%22;var%20t,a=e.length,n=[1732584193,-271733879,-1732584194,271733878];for(t=64;t%3C=e.length;t+=64){for(var%20o=e.substring(t-64,t),i=[],s=void%200,s=0;64%3Es;s+=4)i[s%3E%3E2]=o.charCodeAt(s)+(o.charCodeAt(s+1)%3C%3C8)+(o.charCodeAt(s+2)%3C%3C16)+(o.charCodeAt(s+3)%3C%3C24);r(n,i)}for(e=e.substring(t-64),o=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],t=0;t%3Ce.length;t++)o[t%3E%3E2]|=e.charCodeAt(t)%3C%3C(t%254%3C%3C3);if(o[t%3E%3E2]|=128%3C%3C(t%254%3C%3C3),t%3E55)for(r(n,o),t=0;16%3Et;t++)o[t]=0;return%20o[14]=8*a,r(n,o),n}function%20s(e){for(var%20r=0;r%3Ce.length;r++){for(var%20t=e[r],a=%22%22,n=0;4%3En;n++)a+=u[t%3E%3E8*n+4%2615]+u[t%3E%3E8*n%2615];e[r]=a}return%20e.join(%22%22)}function%20c(e,r){return%20e+r%264294967295}function%20l(e,r){for(var%20t=0;t%3Cr.length;t++)if(r[t]==e)return!0;return!1}function%20f(e){for(var%20r=p,t=0,a=0;a%3Cr.length;a++)l(r[a],e)%26%26t++;return%20t}var%20u=%220123456789abcdef%22.split(%22%22);%225d41402abc4b2a76b9719d911017c592%22!=s(i(%22hello%22))%26%26(c=function(e,r){var%20t=(65535%26e)+(65535%26r);return(e%3E%3E16)+(r%3E%3E16)+(t%3E%3E16)%3C%3C16|65535%26t}),aDigits=%220123456789%22,aLowers=%22abcdefghijklmnopqrstuvwxyz%22,aUppers=%22ABCDEFGHIJKLMNOPQRSTUVWXYZ%22,aSpecs=%22_%22,regExp=/(.*%3F\\.){0,}(.+)\\..+/g,key=e(" + (bool ? "prompt(%22Введите кодовую фразу%22)" : "%22" + $(".secret").val() + "%22") + "),site=e(document.location.host).toLowerCase(),site=regExp.exec(site)[2];for(var%20p=%22%22,h=[],g=0,d=%22%22,d=s(i(site+key)),g=parseInt(d.slice(29,31),16)%252+11,v=0;v!=2*g;)p+=(aDigits+aLowers+aUppers+aSpecs)[parseInt(d.slice(v,v+2),16)%2563],v+=2;var%20w=f(aLowers),C=f(aUppers),b=f(aSpecs);2%3Ef(aDigits)%26%26(h[h.length]=aDigits),2%3Ew%26%26(h[h.length]=aLowers),2%3EC%26%26(h[h.length]=aUppers),2%3Eb%26%26(h[h.length]=aSpecs);for(var%20m=parseInt(d.slice(25,27),16)%254,v=0;v%3Ch.length;v++)for(alphabet=h[v];2%3Ef(alphabet);){var%20U=p[m%25g];if(2%3Cf(l(U,aDigits)%3FaDigits:l(U,aLowers)%3FaLowers:l(U,aUppers)%3FaUppers:aSpecs)){newChar=alphabet[parseInt(d.slice(24,26),16)%25alphabet.length];var%20S=p,A=m%25g,L=newChar;begin=S.slice(0,A),end=S.slice(A+1),p=begin+L+end}m+=7}pwd=p,%22INPUT%22===document.activeElement.tagName.toUpperCase()%3Fdocument.activeElement.value=pwd:prompt(%22Ваш%20пароль%22,pwd),pwd=key=site=%22%22}();");
}

$("a.b_wo").click(function() {
	getBookmarklet(true);
});

$("a.b_w").click(function() {
	getBookmarklet(false);
});

$("input.site").focusin(function() {
	$("submit.get").html("Get my pass");
});

$("input.site").keypress(function (e) {
  if (e.which == 13) {
    $('submit.get').click();
    return false;
  }
});

$("input.secret").focusin(function() {
	$("submit.get").html("Get my pass");
});

$("input.secret").keypress(function (e) {
  if (e.which == 13) {
    $('submit.get').click();
    return false;
  }
});

$('submit.get').click(function() {
	if ($("input.site").val().length == 0) {
		alert("Вы не ввели адрес сайта либо название сервиса");
		return false;
	}
	if ($("input.secret").val().length == 0) {
		alert("Вы уверены, что Вам необходима именно эта кодовая фраза?");
	}
	
	var regExp = /(.*?\.){0,}(.+)\..+/g;
	var site = $("input.site").val().toLowerCase();
	try {
		name = regExp.exec(site)[2];
	} catch (d) {
		name = site;
	}
	pwd = getPassword(name, $("input.secret").val());
	$("submit.get").html(pwd);

//We select password here
	if (s = window.getSelection()) { 
		if(s.setBaseAndExtent) { 
			s.setBaseAndExtent(this, 0, this, this.innerText.length - 1); 
		} else {
			var r = document.createRange(); 
			r.selectNodeContents(this); 
			s.removeAllRanges(); 
			s.addRange(r);
		} 
	} else {
		if (s = document.getSelection){ 
			var s = document.getSelection(); 
			var r = document.createRange(); 
			r.selectNodeContents(this); 
			s.removeAllRanges(); 
			s.addRange(r); 
		} else {
			if (document.selection) { 
				var r = document.body.createTextRange(); 
				r.moveToElementText(this); 
				r.select();
			}
		}
	}
});

/***************** Flexsliders ******************/

$(window).load(function() {

	$('#portfolioSlider').flexslider({
		animation: "slide",
		directionNav: false,
		controlNav: true,
		touch: false,
		pauseOnHover: true,
		start: function() {
			$.waypoints('refresh');
		}
	});

	$('#servicesSlider').flexslider({
		animation: "slide",
		directionNav: false,
		controlNav: true,
		touch: true,
		pauseOnHover: true,
		start: function() {
			$.waypoints('refresh');
		}
	});

	$('#teamSlider').flexslider({
		animation: "slide",
		directionNav: false,
		controlNav: true,
		touch: true,
		pauseOnHover: true,
		start: function() {
			$.waypoints('refresh');
		}
	});

});
