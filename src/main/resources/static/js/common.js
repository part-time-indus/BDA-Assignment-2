
var GENDERS  = ['MALE', 'FEMALE', 'OTHER'];
var STATUSES = ['ACTIVE', 'ONLEAVE', 'RESIGNED'];

var _deptCache = null;


// Turns an enum-style value like "ON_LEAVE" into a readable label like "On leave".
function pretty(v) {
  return String(v).replace(/_/g, ' ').toLowerCase().replace(/^\w/, function (c) { return c.toUpperCase(); });
}

// Shows a small message at the bottom of the screen (green, or red when type is 'err') for 3 seconds.
function toast(msg, type) {
  var $t = $('#toast');
  $t.text(msg).toggleClass('err', type === 'err').addClass('show');
  clearTimeout(toast._t);
  toast._t = setTimeout(function () { $t.removeClass('show'); }, 3000);
}

// Fills a <select> with options from a list, with an optional placeholder, and preselects a value.
function fillSelect($sel, values, selected, placeholder) {
  $sel.empty();
  if (placeholder) { $sel.append($('<option>').val('').text(placeholder)); }
  $.each(values, function (_, v) {
    var isObj = typeof v === 'object';
    $sel.append($('<option>').val(isObj ? v.value : v).text(isObj ? v.label : pretty(v)));
  });
  if (selected !== undefined && selected !== null && selected !== '') { $sel.val(String(selected)); }
}

// Reads the existing /departments page and extracts id + name (no extra backend endpoint needed). 
function loadDepartments(cb) {
  if (_deptCache) { cb(_deptCache); return; }
  $.get(APP_CTX + '/departments').done(function (html) {
    var $d = $('<div>').append($.parseHTML(html));
    var list = [];
    $d.find('#deptTable tbody tr[data-id]').each(function () {
      list.push({ value: $(this).attr('data-id'), label: $(this).attr('data-name') });
    });
    _deptCache = list;
    cb(list);
  }).fail(function () { toast('Could not load departments', 'err'); cb([]); });
}

// Fills select element in scope with genders, statuses or departments.
function initSelects(scope) {
  var $s = scope ? $(scope) : $(document);
  $s.find('select[data-options]').each(function () {
    var $el = $(this), kind = $el.attr('data-options'),
        sel = $el.attr('data-selected'), ph = $el.attr('data-placeholder');
    if (kind === 'gender') { fillSelect($el, GENDERS, sel, ph); }
    else if (kind === 'status') { fillSelect($el, STATUSES, sel, ph); }
    else if (kind === 'department') {
      loadDepartments(function (list) { fillSelect($el, list, sel, ph); });
    }
  });
}


// Replaces department IDs in the doctor table with department names.
function fillDeptNames() {
  var $cells = $('.dept-cell');
  if (!$cells.length) { return; }
  loadDepartments(function (list) {
    var map = {};
    $.each(list, function (_, d) { map[d.value] = d.label; });
    $cells.each(function () {
      var n = map[$(this).attr('data-dept')];
      if (n) { $(this).text(n); }
    });
  });
}

// Fetches a list URL with AJAX and swaps only the <tbody> of the given table. 
function loadTable(url, tableSel) {
  $.get(url).done(function (html) {
    var $d = $('<div>').append($.parseHTML(html));
    var $body = $d.find(tableSel + ' tbody');
    if (!$body.length) { toast('Nothing found', 'err'); return; }
    $(tableSel + ' tbody').html($body.html());
    initSelects($(tableSel));
    fillDeptNames();
  }).fail(function (xhr) { toast('Request failed (' + xhr.status + ')', 'err'); });
}

/* Any <form class="js-form"> is submitted with AJAX.
   data-next="/path" -> redirect there on success; otherwise show a toast and trigger "saved". */
$(document).on('submit', 'form.js-form', function (e) {
  e.preventDefault();
  var $f = $(this), $btn = $f.find('button[type=submit]');
  // blank optional fields (data-blank-null) are not sent, so they are stored as NULL
  var $blank = $f.find('[data-blank-null]').filter(function () { return $.trim(this.value) === ''; }).prop('disabled', true);
  var data = $f.serialize();
  $blank.prop('disabled', false);
  $btn.prop('disabled', true);
  $.post($f.attr('action'), data).done(function () {
    var next = $f.attr('data-next');
    if (next) { window.location.href = APP_CTX + next; }
    else { toast('Saved'); $f.trigger('saved'); }
  }).fail(function (xhr) {
    toast('Could not save (error ' + xhr.status + '). Check the values and try again.', 'err');
  }).always(function () { $btn.prop('disabled', false); });
});

// Delete buttons 
$(document).on('click', '.js-delete', function () {
  var $b = $(this);
  var msg = 'Delete ' + $b.attr('data-name') + '?';
  if ($b.attr('data-warn')) { msg += '\n' + $b.attr('data-warn'); }
  if (!confirm(msg)) { return; }
  $.post(APP_CTX + $b.attr('data-url')).done(function () {
    $b.closest('tr').fadeOut(150, function () { $(this).remove(); });
    toast('Deleted');
  }).fail(function (xhr) { toast('Could not delete (error ' + xhr.status + ')', 'err'); });
});

$(function () { initSelects(); fillDeptNames(); });
