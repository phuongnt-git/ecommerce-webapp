(function ($) {
    'use strict';

    /*---------------------------
    Preloader
    ---------------------------*/
    $(window).on('load', function () {
        var preLoder = $(".preloader");
        preLoder.fadeOut(1000);
    });

    /*---------------------------
    Fixed Header
    ---------------------------*/
    $(window).on("scroll", function () {
        if ($(window).width() > 1199) {
            var scrolling = $(this).scrollTop();
            if (scrolling > 300) {
                var toggler = $(".all-department").width();
                $(".header .menu-bar").addClass("navbar-fixed");
                $(".category-list").slideUp(1).addClass("fixed").css('width', toggler);
                $(".header-inner .bottom-header").addClass("fixed-header");
                $('.all-department').find('i').removeClass('fa-times').addClass('fa-bars')
            } else {
                $(".header .menu-bar").removeClass("navbar-fixed");
                $(".category-list").slideDown(300).removeClass("fixed").css('width', '100%');
                $(".header-inner .bottom-header").removeClass("fixed-header");
            }
        } else {
            var scrolling = $(this).scrollTop();
            if (scrolling > 300) {
                var toggler = $(".all-department").width();
                $(".header .menu-bar").addClass("navbar-fixed");
                $(".category-list").addClass("fixed");
                $(".header-inner .bottom-header").addClass("fixed-header");
            } else {
                $(".header .menu-bar").removeClass("navbar-fixed");
                $(".category-list").removeClass("fixed");
                $(".header-inner .bottom-header").removeClass("fixed-header");
            }
        }
    });

    $(document).ready(function () {

        /*---------------------------
        Search Bar In Small Devices
        ---------------------------*/
        if ($(window).width() < 576) {
            $('.search-col').hide();
            $('.search-open').on('click', function () {
                $('.search-col').slideToggle(300);
            })
        }

        /*---------------------------
        Main Category List In Inner Pages
        ---------------------------*/
        $(".category-list-btn").on("click", function () {
            $(this).toggleClass("clicked");
            $(this).children("i").toggleClass("fa-times fa-bars");
            $(".category-list-2").slideToggle(300);
        });

        /*---------------------------
        Niceselect Plugin Init
        ---------------------------*/
        $('.select').niceSelect();

        /*---------------------------
        Language Selection Menu
        ---------------------------*/
        $('#aw').flagStrap({
            countries: {
                "UK": "English",
                "BD": "Bangla"
            },
            buttonSize: "",
            buttonType: "",
            buttonText: "Country of",
            labelMargin: "10px",
            scrollable: false,
            scrollableHeight: "350px"
        });

        /*---------------------------
        Cart List Open & Close
        ---------------------------*/
        $(".cart-list-btn").on('click', function () {
            $("body").addClass("clear");
            $("#headerCartWrap").addClass("active");
        });
        $(".cart-close").on('click', function () {
            $("body").removeClass("clear");
            $("#headerCartWrap").removeClass("active");
        });

        /*---------------------------
        Wish List Open & Close
        ---------------------------*/
        $(".wish-list-btn").on('click', function () {
            $("body").addClass("clear");
            $("#headerWishWrap").addClass("active");
        });
        $(".wish-close").on('click', function () {
            $("body").removeClass("clear");
            $("#headerWishWrap").removeClass("active");
        });

        /*---------------------------
        Category List Close
        ---------------------------*/
        $(".category-list-close").on('click', function () {
            $(".category-list").slideToggle(300);
            $(this).find('i').toggleClass('fa-times fa-bars');
        });
        $(".category-list ul li.has-sub, .category-list-2 ul li.has-sub").append("<span class='arrow'></span>").children('a').removeAttr('href').attr('role', 'button');
        if ($(window).width() < 1200) {
            $('.category-list').hide();
        }

        /*---------------------------
        Mobile Menu
        ---------------------------*/
        $('.m-nav-link').on('click', function () {
            var target = $(this).data('target');
            $('.' + target).addClass('active')
        });

        $(".mobile-category-list ul li.has-sub").append("<span class='plus'><i class='fa-regular fa-plus'></i></span>").children('a').removeAttr('href').attr('role', 'button');
        $('.mobile-category-list .category-item.has-sub a').on('click', function () {
            $(this).siblings('.plus').find('i').toggleClass('fa-minus fa-plus');
            $(this).siblings('.category-sub-menu').slideToggle(300);
            $(this).parent().siblings().find('.category-sub-menu').slideUp(300).parent().find('.plus i').addClass('fa-plus').removeClass('fa-minus');
        });
        $('.mobile-menu-close-btn').on('click', function () {
            $(this).parent().removeClass('active').find('.category-sub-menu').slideUp();
        });

        /*---------------------------
        Slider In Banner
        ---------------------------*/
        $('.slider-area').slick({
            dots: true,
            arrows: false,
            autoplay: true,
            pauseOnFocus: false,
            pauseOnHover: false,
        });

        $('.product-slider-1').slick({
            slidesToShow: 4,
            vertical: true,
            verticalSwiping: true,
            prevArrow: '<button type="button" class="slick-prev"><i class="fa-solid fa-angle-up"></i></button>',
            nextArrow: '<button type="button" class="slick-next"><i class="fa-solid fa-angle-down"></i></button>',
            autoplay: true,
            pauseOnFocus: false,
            pauseOnHover: false,
            responsive: [
                {
                    breakpoint: 1024,
                    settings: {}
                },
                {
                    breakpoint: 992,
                    settings: {
                        slidesToShow: 3,
                        vertical: false,
                        verticalSwiping: false,
                        prevArrow: '<button type="button" class="slick-prev"><i class="fa-solid fa-angle-left"></i></button>',
                        nextArrow: '<button type="button" class="slick-next"><i class="fa-solid fa-angle-right"></i></button>',
                    }
                },
                {
                    breakpoint: 768,
                    settings: {
                        slidesToShow: 2,
                        vertical: false,
                        verticalSwiping: false,
                        prevArrow: '<button type="button" class="slick-prev"><i class="fa-solid fa-angle-left"></i></button>',
                        nextArrow: '<button type="button" class="slick-next"><i class="fa-solid fa-angle-right"></i></button>',
                    }
                },
                {
                    breakpoint: 480,
                    settings: {
                        vertical: true,
                        verticalSwiping: true,
                        prevArrow: '<button type="button" class="slick-prev"><i class="fa-solid fa-angle-up"></i></button>',
                        nextArrow: '<button type="button" class="slick-next"><i class="fa-solid fa-angle-down"></i></button>',
                    }
                }
            ]
        });

        /*---------------------------
        Slider In Banner In Home Page 2
        ---------------------------*/
        $('.banner-slider').slick({
            slidesToShow: 1,
            dots: true,
            arrows: false,
            autoplay: true,
            pauseOnFocus: false,
            pauseOnHover: false,
            speed: 2000,
            fade: true,
        });

        /*---------------------------
        Flash Deal Countdown
        ---------------------------*/
        $('#flashDealCountdown').syotimer({
            layout: 'dhms',
            periodUnit: 'd',
            periodic: true,
            periodInterval: 2
        });

        /*---------------------------
        Product Image Popup
        ---------------------------*/
        $(".single-product-card .quick-view").on('click', function () {
            var image = $(this).parents(".cart-option").siblings(".part-img").find("img").attr("src");
            $(".product-quick-view-panel").addClass("active");
            $(".quick-view-image").attr("src", image);
            $("body").css('overflow', 'hidden');
        });
        $(".product-quick-view-panel").on("click", function (e) {
            if ($(e.target).is(".quick-view-image") === false) {
                $(this).removeClass("active");
                $("body").css('overflow', 'auto');
            }
        });

        /*---------------------------
        Hot Deal Countdown
        ---------------------------*/
        $('#hotDealCountdown').syotimer({
            layout: 'dhms',
            periodUnit: 'd',
            periodic: true,
            periodInterval: 7,
        });

        /*---------------------------
        Recent View Slider
        ---------------------------*/
        $('.recent-view-slider').owlCarousel({
            items: 3,
            margin: 24,
            nav: true,
            navText: ['<i class="fa-solid fa-angle-left"></i>', '<i class="fa-solid fa-angle-right"></i>'],
            dots: false,
            smartSpeed: 1000,
            responsive: {
                0: {
                    items: 1,
                },
                480: {
                    items: 2,
                    margin: 15
                },
                768: {
                    items: 1,
                },
                992: {
                    items: 2,
                    margin: 15
                },
                1200: {
                    items: 3,
                    margin: 15
                }
            }
        });
        $('.recent-view-slider .owl-nav').prependTo('.recent-view-slide-nav');

        /*---------------------------
        Tpo Rated Slider
        ---------------------------*/
        $('.left-slider').slick({
            slidesToShow: 4,
            vertical: true,
            verticalSwiping: true,
            prevArrow: '<button type="button" class="slick-prev"><i class="fa-solid fa-angle-up"></i></button>',
            nextArrow: '<button type="button" class="slick-next"><i class="fa-solid fa-angle-down"></i></button>',
            autoplay: true,
            pauseOnFocus: false,
            pauseOnHover: false,
            asNavFor: '.right-slider',
            speed: 2000
        });
        $('.right-slider').slick({
            slidesToShow: 4,
            vertical: true,
            verticalSwiping: true,
            arrows: false,
            autoplay: true,
            pauseOnFocus: false,
            pauseOnHover: false,
            asNavFor: '.left-slider',
            speed: 2000
        });
        $('.left-slider .slick-arrow').prependTo('.top-rated-slider-nav');


        /*---------------------------
        Testimonial Slider
        ---------------------------*/
        $('.review-slider').slick({
            slidesToShow: 4,
            arrows: false,
            autoplay: true,
            pauseOnFocus: false,
            pauseOnHover: false,
            responsive: [
                {
                    breakpoint: 1200,
                    settings: {
                        slidesToShow: 3,
                    }
                },
                {
                    breakpoint: 992,
                    settings: {
                        slidesToShow: 2,
                    }
                },
                {
                    breakpoint: 576,
                    settings: {
                        slidesToShow: 1,
                        slidesToScroll: 1
                    }
                }
            ]
        });


        /*---------------------------
        Blog Slider
        ---------------------------*/
        $('.blog-slider').slick({
            slidesToShow: 3,
            arrows: false,
            autoplay: true,
            pauseOnFocus: false,
            pauseOnHover: false,
            responsive: [
                {
                    breakpoint: 1200,
                    settings: {
                        slidesToShow: 2,
                    }
                },
                {
                    breakpoint: 768,
                    settings: {
                        slidesToShow: 1,
                    }
                }
            ]
        });


        /*---------------------------
        Feature Section Transform
        ---------------------------*/
        var featureHeight = $("#feature").height();
        var halfHeight = featureHeight / 2;
        $("#feature").css('margin-bottom', -halfHeight);
        $(".footer").css('padding-top', halfHeight);


        /*---------------------------
        FAQ Section Icon Change
        ---------------------------*/
        $('.collapse').on('shown.bs.collapse', function () {
            $(this).parent().find('.fa-plus').removeClass('fa-plus').addClass('fa-minus');
        }).on('hidden.bs.collapse', function () {
            $(this).parent().find('.fa-minus').removeClass('fa-minus').addClass('fa-plus');
        });


        /*---------------------------
        Video Popup
        ---------------------------*/
        $(".js-modal-btn").modalVideo();


        /*---------------------------
        Shop Details Page Product Image View
        ---------------------------*/
        //===== 01. gallery view
        $(".small-thumb").on("click", function () {
            var thumbImage = $(this).find("img").attr("src");
            $(this).addClass("active").siblings().removeClass("active");
            $("#bigPreview").find("img").attr("src", thumbImage);
        });
        //===== 02. Image popup and close
        $("#bigPreview .quick-view").on('click', function () {
            var image = $(this).siblings("img").attr("src");
            $(".product-quick-view-panel-2").addClass("active");
            $(".quick-view-image").attr("src", image);
            $("body").css('overflow', 'hidden');
        });
        $(".panel-content").on("click", function (e) {
            if ($(e.target).is(".quick-view-image") === false) {
                $(".product-quick-view-panel-2").removeClass("active");
                $("body").css('overflow', 'auto');
            }
        });
        //===== 03. gallery previous/next function
        $("#prevImg").on("click", function () {
            if ($(".small-thumb.active").prev().is(".small-thumb")) {
                var prevImg = $(".small-thumb.active").prev(".small-thumb").find("img").attr("src");
                $(".small-thumb.active").removeClass("active").prev().addClass("active");
                $(".quick-view-image").attr("src", prevImg);
                $("#bigPreview").find("img").attr("src", prevImg);
            } else {
                $(".notification").addClass("active");
                setTimeout(function () {
                    $(".notification").removeClass("active");
                }, 1000);
            }
        });
        $("#nextImg").on("click", function () {
            if ($(".small-thumb.active").next().is(".small-thumb")) {
                var nextImg = $(".small-thumb.active").next(".small-thumb").find("img").attr("src");
                $(".small-thumb.active").removeClass("active").next().addClass("active");
                $(".quick-view-image").attr("src", nextImg);
                $("#bigPreview").find("img").attr("src", nextImg);
            } else {
                $(".notification").addClass("active");
                setTimeout(function () {
                    $(".notification").removeClass("active");
                }, 1000);
            }
        });


        /*---------------------------
        Product Details Page Color Selection
        ---------------------------*/
        $(".color-select .list .option").each(function () {
            var color = $(this).text();
            $(this).append("<span class='circle'></span>");
            $(this).find(".circle").css('background', color);
        });
        $(".color-select .current").after("<span class='selected-circle'></span>");
        $(".color-select .option").on("click", function () {
            var color = $(this).text();
            $(".selected-circle").css('background', color);
        });


        /*---------------------------
        Product Quantity
      
        $('.quantity').each(function () {
            var spinner = jQuery(this),
                input = spinner.find('input[type="number"]'),
                btnUp = spinner.find('.quantity-up'),
                btnDown = spinner.find('.quantity-down'),
                min = input.attr('min'),
                max = input.attr('max');

            btnUp.on('click', function () {
                var oldValue = parseFloat(input.val());
                if (oldValue >= max) {
                    var newVal = oldValue;
                } else {
                    var newVal = oldValue + 1;
                }
                spinner.find("input").val(newVal);
                spinner.find("input").trigger("change");
            });

            btnDown.on('click', function () {
                var oldValue = parseFloat(input.val());
                if (oldValue <= min) {
                    var newVal = oldValue;
                } else {
                    var newVal = oldValue - 1;
                }
                spinner.find("input").val(newVal);
                spinner.find("input").trigger("change");
            });

        });
  ---------------------------*/

        /*---------------------------
        Review Image Popup
        ---------------------------*/
        $(".client-img").on("click", function () {
            var clientProduct = $(this).find("img").attr("src");
            $(this).addClass("active").siblings().removeClass("active");
            $(this).parent().siblings(".view-panel").addClass("active");
            $(this).parents(".single-review").siblings().find(".client-img").removeClass("active");
            $(this).parents(".single-review").siblings().find(".view-panel").removeClass("active");
            $(".client-product").attr("src", clientProduct);
        });
        $(".clt-view-panel-close").on("click", function () {
            $(".client-product").attr("src", "");
            $(".gallery-wrap .view-panel").removeClass("active");
            $(".client-img").removeClass("active");
        });

        /*---------------------------
        Related Product Slider
        ---------------------------*/
        $('.related-product-slider').owlCarousel({
            items: 5,
            margin: 30,
            autoplay: true,
            loop: true,
            smartSpeed: 1000,
            dots: false,
            responsive: {
                0: {
                    items: 2,
                    margin: 15
                },
                480: {
                    items: 2,
                    margin: 15
                },
                768: {
                    items: 3,
                    margin: 15
                },
                992: {
                    items: 4,
                    margin: 15
                },
                1200: {
                    items: 5,
                    margin: 15
                }
            }
        });
    });
})(jQuery);