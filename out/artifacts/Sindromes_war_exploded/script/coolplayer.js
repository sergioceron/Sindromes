function get_nav_language() {
    var nav_lng;
    if(navigator.userlanguage) nav_lng = navigator.userlanguage;
    if(navigator.browserLanguage) nav_lng = navigator.browserLanguage;
    if(navigator.systemLanguage) nav_lng = navigator.systemLanguage;
    if(navigator.language) nav_lng = navigator.language;
    return new String(nav_lng).toLowerCase();
}

var coolplayer_nav_lng = get_nav_language();
var coolplayer_nav_language = "en, zh-cn, zh-tw";
if (coolplayer_nav_language.indexOf(coolplayer_nav_lng) == -1) coolplayer_nav_lng = 'en';

var coolplayer_lang = [];
coolplayer_lang['en'] = [];
coolplayer_lang['en']['loading'] = "Loading...";
coolplayer_lang['en']['notsupport'] = "This media can NOT be support to embed into the page, <br />but you can download it by the following link.";
coolplayer_lang['en']['waiting'] = "Wait a moment, please!";
coolplayer_lang['en']['url'] = "URL:";
coolplayer_lang['en']['fullscreen'] = "FullScreen";


coolplayer_lang['zh-cn'] = [];
coolplayer_lang['zh-cn']['loading'] = "&#36733;&#20837;&#20013;&#8230;&#8230;";
coolplayer_lang['zh-cn']['notsupport'] = "&#35813;&#23186;&#20307;&#19981;&#25903;&#25345;&#22312;&#39029;&#38754;&#20869;&#23884;&#20837;&#26174;&#31034;&#65292;<br />&#20294;&#20320;&#21487;&#20197;&#36890;&#36807;&#19979;&#38754;&#30340;&#36830;&#25509;&#19979;&#36733;&#23427;&#12290;";
coolplayer_lang['zh-cn']['waiting'] = "&#35831;&#31245;&#20505;&#65281;";
coolplayer_lang['zh-cn']['url'] = "&#22320;&#22336;&#65306;";
coolplayer_lang['zh-cn']['fullscreen'] = "&#20840;&#23631;&#25773;&#25918;";

coolplayer_lang['zh-tw'] = [];
coolplayer_lang['zh-tw']['loading'] = "&#36617;&#20837;&#20013;&#8230;&#8230;";
coolplayer_lang['zh-tw']['notsupport'] = "&#35442;&#23186;&#39636;&#19981;&#25903;&#25588;&#22312;&#38913;&#38754;&#20839;&#23884;&#20837;&#39023;&#31034;&#65292;<br />&#20294;&#20320;&#21487;&#20197;&#36890;&#36942;&#19979;&#38754;&#30340;&#36899;&#25509;&#19979;&#36617;&#23427;&#12290;";
coolplayer_lang['zh-tw']['waiting'] = "&#35531;&#31245;&#20505;&#65281;";
coolplayer_lang['zh-tw']['url'] = "&#22320;&#22336;&#65306;";
coolplayer_lang['zh-tw']['fullscreen'] = "&#20840;&#23631;&#25773;&#25918;";

function coolplayer(url, id, width, height, autoplay, loop, charset, mediatype) {
    var result = ['<span class="coolplayer_info" style="width: ',
        (parseInt(width) - 2),
        'px; border-top: 0; border-bottom: 0; padding: 12px 0;">',
        coolplayer_lang[coolplayer_nav_lng]['loading'],
        '</span>'].join('');
    if (document.getElementById('coolplayer_playlist_' + id)) {
        document.getElementById('coolplayer_playlist_' + id).style.width = (parseInt(width) - 2) + "px";
    }
    if (document.getElementById('coolplayer_info_' + id)) {
        document.getElementById('coolplayer_info_' + id).style.width = (parseInt(width) - 2) + "px";
    }
    document.getElementById('coolplayer_container_' + id).innerHTML = result;
    document.getElementById('coolplayer_info_' + id).innerHTML = coolplayer_lang[coolplayer_nav_lng]['waiting'];
    coolplayer_rpc.play_media(url, id, width, height, autoplay, loop, charset, mediatype);
}

function coolplayer_flash(id, src, mime, width, height, autoplay, loop, url, info) {
    var result = ['<![if !IE]><object data="', src, '" type="', mime, '" ',
        'width="', width, '" height="', height, '" class="coolplayer_moz">',
        '<param name="allowScriptAccess" value="always" />',
        '<param name="allowFullScreen" value="true" />',
        '<param name="play" value="', autoplay, '" />',
        '<param name="loop" value="', loop, '" />',
        '<param name="flashvars" value="isNotEmbed=1" />',
        '<param name="quality" value="high" /></object><![endif]>',
        '<!--[if IE]><object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" ',
        'width="', width, '" height="', height, '">',
        '<param name="movie" value="', src, '" />',
        '<param name="allowScriptAccess" value="always" />',
        '<param name="allowFullScreen" value="true" />',
        '<param name="play" value="', autoplay, '" />',
        '<param name="loop" value="', loop, '" />',
        '<param name="flashvars" value="isNotEmbed=1" />',
        '<param name="quality" value="high" />',
        '</object><![endif]-->'].join('');
    document.getElementById('coolplayer_container_' + id).innerHTML = result;
    document.getElementById('coolplayer_info_' + id).innerHTML = ['<a href="', url, '">', info, '</a>'].join('');
}

function coolplayer_qt(id, src, mime, width, height, autoplay, loop, url, info) {
    var result = ['<![if !IE]><object data="', src, '" type="', mime, '" ',
        'width="', width, '" height="', height, '" class="coolplayer_moz">',
        '<param name="controller" value="true" />',
        '<param name="scale" value="tofit" />',
        '<param name="autoplay" value="', autoplay, '" />',
        '<param name="loop" value="', loop, '" /></object><![endif]>',
        '<!--[if IE]><object classid="clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B" ',
        'width="', width, '" height="', height, '">',
        '<param name="controller" value="true" />',
        '<param name="scale" value="tofit" />',
        '<param name="autoplay" value="', autoplay, '" />',
        '<param name="loop" value="', loop, '" />',
        '<param name="src" value="', src, '" />',
        '</object><![endif]-->'].join('');
    document.getElementById('coolplayer_container_' + id).innerHTML = result;
    document.getElementById('coolplayer_info_' + id).innerHTML = ['<a href="', url, '">', info, '</a>'].join('');
}

function coolplayer_rm(id, src, mime, width, height, autoplay, loop, url, info) {
    var result = ['<object classid="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA" ',
        'width="', width, '" height="', height, '" id="rm_', id, '">',
        '<param name="src" value="', src, '" />',
        '<param name="controls" value="Imagewindow" />',
        '<param name="console" value="clip', id, '" />',
        '<param name="autostart" value="', autoplay, '" />',
        '<param name="loop" value="', loop, '" />',
        '<embed src="', src, '" type="audio/x-pn-realaudio-plugin" ',
        'autostart="', autoplay, '" loop="', loop, '" console="clip', id, '" ',
        'width="', width, '" height="', height, '" ', 'name="rm_', id, '" ',
        'controls="Imagewindow"></embed><br />',
        '</object>',
        '<object classid="clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA" ',
        'width="', width - 42, '" height="42">',
        '<param name="src" value="', src, '" />',
        '<param name="controls" value="ControlPanel" />',
        '<param name="console" value="clip', id, '" />',
        '<param name="autostart" value="', autoplay, '" />',
        '<param name="loop" value="', loop, '" />',
        '<embed src="', src, '" type="audio/x-pn-realaudio-plugin" ',
        'autostart="', autoplay, '" loop="', loop, '" console="clip', id, '" ',
        'width="', width - 42, '" height="42" ',
        'controls="ControlPanel"></embed>',
        '</object>',
        '<![if !IE]><img src="', coolplayer_plugin_path,
        '/fullscreen.gif" width="42" height="42" alt="', coolplayer_lang[coolplayer_nav_lng]['fullscreen'],
        '" title="', coolplayer_lang[coolplayer_nav_lng]['fullscreen'],
        '" onclick="document.rm_', id, '.SetFullScreen()" style="cursor: pointer"><![endif]>',
        '<!--[if IE]><img src="', coolplayer_plugin_path,
        '/fullscreen.gif" width="42" height="42" alt="', coolplayer_lang[coolplayer_nav_lng]['fullscreen'],
        '" title="', coolplayer_lang[coolplayer_nav_lng]['fullscreen'],
        '" onclick="document.getElementById(\'rm_',
        id, '\').SetFullScreen()"  title="" style="cursor: pointer"><![endif]-->'].join('');
    document.getElementById('coolplayer_container_' + id).innerHTML = result;
    document.getElementById('coolplayer_info_' + id).innerHTML = ['<a href="', url, '">', info, '</a>'].join('');
}

function coolplayer_wm(id, src, mime, width, height, autoplay, loop, url, info) {
/*@cc_on @*/
/*@if (@_jscript)
    var result = ['<object classid="clsid:6BF52A52-394A-11d3-B153-00C04F79FAA6" ',
        'width="', width, '" height="', height, '">',
        '<param name="autoStart" value="', autoplay, '" />',
        '<param name="loop" value="', loop, '" />',
        '<param name="URL" value="', src, '" />',
        '</object>'].join('');
@else @*/
    if (src.substr(0, 4) != 'mms:') {
        var result = ['<object width="', width, '" height="', height, '" data="', src, '" type="', mime ,'">',
            '<param name="autoStart" value="', autoplay, '" />',
            '<param name="loop" value="', loop, '" />',
            '<param name="ShowStatusBar" value="1" />',
             '</object>'].join('');
    }
    else {
        var result = ['<object width="', width, '" height="', height, '" data="', src, '" type="', mime ,'">',
            '<param name="autoStart" value="1" />',
            '<param name="loop" value="0" />',
            '<param name="ShowStatusBar" value="1" />',
             '</object>'].join('');
    }
/*@end @*/
    document.getElementById('coolplayer_container_' + id).innerHTML = result;
    document.getElementById('coolplayer_info_' + id).innerHTML = ['<a href="', url, '">', info, '</a>'].join('');
}

function coolplayer_pdf(id, src, mime, width, height, autoplay, loop, url, info) {
    var result = ['<![if !IE]><object data="', src, '" type="', mime, '" ',
        'width="', width, '" height="', height, '" class="coolplayer_moz">',
        '</object><![endif]>',
        '<!--[if IE]><object classid="clsid:CA8A9780-280D-11CF-A24D-444553540000" ',
        'width="', width, '" height="', height, '">',
        '<param name="src" value="', src, '" />',
        '</object><![endif]-->'].join('');
    document.getElementById('coolplayer_container_' + id).innerHTML = result;
    document.getElementById('coolplayer_info_' + id).innerHTML = ['<a href="', url, '">', info, '</a>'].join('');
}

function coolplayer_img(id, src, mime, width, height, autoplay, loop, url, info) {
    var result = ['<img src="', src, '" style="width: ', width, 'px; height: ', height, 'px" alt="" />'].join('');
    document.getElementById('coolplayer_container_' + id).innerHTML = result;
    document.getElementById('coolplayer_info_' + id).innerHTML = ['<a href="', url, '">', info, '</a>'].join('');
}

function coolplayer_dcr(id, src, mime, width, height, autoplay, loop, url, info) {
    var result = ['<![if !IE]><object data="', src, '" type="application/x-director" ',
        'width="', width, '" height="', height, '" class="coolplayer_moz">',
        '</object><![endif]>',
        '<!--[if IE]><object classid="clsid:166B1BCA-3F9C-11CF-8075-444553540000" ',
        'width="', width, '" height="', height, '" ',
        'codebase="http://fpdownload.macromedia.com/get/shockwave/cabs/director/sw.cab#version=10,1,4,020">',
        '<param name="src" value="', src, '" />',
        '</object><![endif]-->'].join('');
    document.getElementById('coolplayer_container_' + id).innerHTML = result;
    document.getElementById('coolplayer_info_' + id).innerHTML = ['<a href="', url, '">', info, '</a>'].join('');
}

function coolplayer_unknown(id, src, mime, width, height, autoplay, loop, url, info) {
    var result = ['<span class="coolplayer_info" style="width: ',
        (parseInt(width) - 2), 'px; border-top: 0; border-bottom: 0; padding: 12px 0;">',
        coolplayer_lang[coolplayer_nav_lng]['notsupport'],
        '</span>'].join('');
    document.getElementById('coolplayer_container_' + id).innerHTML = result;
    document.getElementById('coolplayer_info_' + id).innerHTML = ['<a href="', url, '">', info, '</a>'].join('');
}

function coolplayer_input(e, width, height, autoplay, loop, charset, mediatype) {
    var id = e.id.substr(16);
    var old_content = e.innerHTML;
    e.innerHTML = coolplayer_lang[coolplayer_nav_lng]['url'];
    var input = document.createElement('input');
    input.type = "text";
    input.style.width = (parseInt(e.style.width) - 60) + "px";
    input.onkeypress = function (event) {
        if (window.event) event = window.event;
        if (event.keyCode == 27) {
            this.blur();
        }
        if (event.keyCode == 13) {
            coolplayer(this.value, id, width, height, autoplay, loop, charset, mediatype);
        }
    }
    input.onblur = function () {
        e.innerHTML = old_content;
    }
    e.appendChild(input);
    input.select();
}

var coolplayer_rpc = new PHPRPC_Client(coolplayer_rpcurl, ['play_media']);

coolplayer_rpc.play_media_callback = function (result, args, output) {
    if (result instanceof PHPRPC_Error) {
        alert(result.toString());
    }
    else {
        var str;
        if (document.getElementById('coolplayer_playlist_' + result['id'])) {
            document.getElementById('coolplayer_playlist_' + result['id']).style.width = (result['width'] - 2) + "px";
        }
        if (document.getElementById('coolplayer_info_' + result['id'])) {
            document.getElementById('coolplayer_info_' + result['id']).style.width = (result['width'] - 2) + "px";
        }
        result['info'] = result['info'].replace(/\'/g, "\\'");
        str = [result['func'], "('",
            [result['id'], result['src'], result['mime'],
             result['width'], result['height'], result['autoplay'],
             result['loop'], result['url'], result['info']].join("', '"),
            "');"].join('');
        eval(str);
    }
}

function FlashRequest(status, args) {
    void(0);  //crack google video
}