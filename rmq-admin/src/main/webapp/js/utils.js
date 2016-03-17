function strlen(str) {

    return str.length;
    //var len = 0;
    //for (var i = 0; str && i < str.length; i++) {
    //    var c = str.charCodeAt(i);
    //    if ((c >= 0x0001 && c <= 0x007e) || (0xff60 <= c && c <= 0xff9f)) {
    //        len++;
    //    } else {
    //        len += 2;
    //    }
    //}
    //return len;
}

var escape = document.createElement('textarea');
function escapeHTML(html) {
    escape.textContent = html;
    return escape.innerHTML;
}

function unescapeHTML(html) {
    escape.innerHTML = html;
    return escape.textContent;
}
