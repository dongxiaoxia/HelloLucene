
var count = 0;
function changeBackgroundColor() {
    var color1 = '#006633';
    var color2 = '##009999';
    var color3 = '#0099FF';
    var color4 = '#33CC99';
    var color5 = '#6699FF';
    var selectColor;
    //var count = Math.floor(Math.random() * 5)
    count++;

    if (count%5 == 1) {
        selectColor = color1;
    } else if (count%5 == 2) {
        selectColor = color2;
    } else if (count%5 == 3) {
        selectColor = color3;
    }
    else if (count%5 == 4) {
        selectColor = color4;

    } else if (count%5 == 0) {
        selectColor = color5;
    }
    $("body").css("background-color", selectColor);
}

window.onload = function () {
    window.setInterval(changeBackgroundColor, 5000);
};


