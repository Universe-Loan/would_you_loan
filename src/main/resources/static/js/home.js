document.addEventListener('DOMContentLoaded', function() {

    // home 중간 배너 슬라이드
    var myCarousel = document.querySelector('#heroCarousel')
    var carousel = new bootstrap.Carousel(myCarousel, {
        interval: 5000, // 5초마다 슬라이드 변경
        wrap: true // 마지막 슬라이드에서 첫 번째로 순환
    })

    // mypage 부동산 등록 팝업

});