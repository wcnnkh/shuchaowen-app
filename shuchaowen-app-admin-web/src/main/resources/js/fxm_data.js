var fxmDate=function(config){
	var t=this;
	t.boxid= config['bigClass']
	t.inputBox= config['inputBox']
}
fxmDate.prototype={
	init(){
		var t=this;
		t.h=$(t.boxid).find('input').height();
		console.log(t.boxid)
		t.str=`<div class="layui-laydate" style="left:0px; top:${t.h}px ;"><div class="layui-laydate-main laydate-main-list-0 laydate-time-show"><div class="layui-laydate-header"><i class="layui-icon laydate-icon laydate-prev-y"></i><i class="layui-icon laydate-icon laydate-prev-m"></i><div class="laydate-set-ym"><span lay-ym="2019-7" lay-type="year">2019年</span><span lay-ym="2019-7" lay-type="month">7月</span><span class="laydate-time-text">选择时间</span></div><i class="layui-icon laydate-icon laydate-next-m"></i><i class="layui-icon laydate-icon laydate-next-y"></i></div><div class="layui-laydate-content"><table><thead><tr><th>日</th><th>一</th><th>二</th><th>三</th><th>四</th><th>五</th><th>六</th></tr></thead><tbody><tr><td class="laydate-day-prev" lay-ymd="2019-6-30">30</td><td lay-ymd="2019-7-1" class="">1</td><td lay-ymd="2019-7-2" class="">2</td><td lay-ymd="2019-7-3" class="">3</td><td lay-ymd="2019-7-4" class="">4</td><td lay-ymd="2019-7-5" class="">5</td><td lay-ymd="2019-7-6" class="">6</td></tr><tr><td lay-ymd="2019-7-7" class="">7</td><td lay-ymd="2019-7-8" class="">8</td><td lay-ymd="2019-7-9" class="">9</td><td lay-ymd="2019-7-10" class="">10</td><td lay-ymd="2019-7-11" class="">11</td><td lay-ymd="2019-7-12" class="">12</td><td lay-ymd="2019-7-13" class="">13</td></tr><tr><td lay-ymd="2019-7-14" class="">14</td><td lay-ymd="2019-7-15" class="">15</td><td lay-ymd="2019-7-16" class="">16</td><td lay-ymd="2019-7-17" class="">17</td><td lay-ymd="2019-7-18" class="">18</td><td lay-ymd="2019-7-19" class="">19</td><td lay-ymd="2019-7-20" class="">20</td></tr><tr><td lay-ymd="2019-7-21" class="">21</td><td lay-ymd="2019-7-22" class="">22</td><td lay-ymd="2019-7-23" class="">23</td><td lay-ymd="2019-7-24" class="">24</td><td lay-ymd="2019-7-25" class="">25</td><td class="layui-this" lay-ymd="2019-7-26">26</td><td lay-ymd="2019-7-27" class="">27</td></tr><tr><td lay-ymd="2019-7-28" class="">28</td><td lay-ymd="2019-7-29" class="">29</td><td lay-ymd="2019-7-30" class="">30</td><td lay-ymd="2019-7-31" class="">31</td><td class="laydate-day-next" lay-ymd="2019-8-1">1</td><td class="laydate-day-next" lay-ymd="2019-8-2">2</td><td class="laydate-day-next" lay-ymd="2019-8-3">3</td></tr><tr><td class="laydate-day-next" lay-ymd="2019-8-4">4</td><td class="laydate-day-next" lay-ymd="2019-8-5">5</td><td class="laydate-day-next" lay-ymd="2019-8-6">6</td><td class="laydate-day-next" lay-ymd="2019-8-7">7</td><td class="laydate-day-next" lay-ymd="2019-8-8">8</td><td class="laydate-day-next" lay-ymd="2019-8-9">9</td><td class="laydate-day-next" lay-ymd="2019-8-10">10</td></tr></tbody></table><ul class="layui-laydate-list laydate-time-list"><li><p>时</p><ol><li class="layui-this">00</li><li class="">01</li><li class="">02</li><li class="">03</li><li class="">04</li><li class="">05</li><li class="">06</li><li class="">07</li><li class="">08</li><li class="">09</li><li class="">10</li><li class="">11</li><li class="">12</li><li class="">13</li><li class="">14</li><li class="">15</li><li class="">16</li><li class="">17</li><li class="">18</li><li class="">19</li><li class="">20</li><li class="">21</li><li class="">22</li><li class="">23</li></ol></li><li><p>分</p><ol><li class="layui-this">00</li><li class="">01</li><li class="">02</li><li class="">03</li><li class="">04</li><li class="">05</li><li class="">06</li><li class="">07</li><li class="">08</li><li class="">09</li><li class="">10</li><li class="">11</li><li class="">12</li><li class="">13</li><li class="">14</li><li class="">15</li><li class="">16</li><li class="">17</li><li class="">18</li><li class="">19</li><li class="">20</li><li class="">21</li><li class="">22</li><li class="">23</li><li class="">24</li><li class="">25</li><li class="">26</li><li class="">27</li><li class="">28</li><li class="">29</li><li class="">30</li><li class="">31</li><li class="">32</li><li class="">33</li><li class="">34</li><li class="">35</li><li class="">36</li><li class="">37</li><li class="">38</li><li class="">39</li><li class="">40</li><li class="">41</li><li class="">42</li><li class="">43</li><li class="">44</li><li class="">45</li><li class="">46</li><li class="">47</li><li class="">48</li><li class="">49</li><li class="">50</li><li class="">51</li><li class="">52</li><li class="">53</li><li class="">54</li><li class="">55</li><li class="">56</li><li class="">57</li><li class="">58</li><li class="">59</li></ol></li><li><p>秒</p><ol><li class="layui-this">00</li><li class="">01</li><li class="">02</li><li class="">03</li><li class="">04</li><li class="">05</li><li class="">06</li><li class="">07</li><li class="">08</li><li class="">09</li><li class="">10</li><li class="">11</li><li class="">12</li><li class="">13</li><li class="">14</li><li class="">15</li><li class="">16</li><li class="">17</li><li class="">18</li><li class="">19</li><li class="">20</li><li class="">21</li><li class="">22</li><li class="">23</li><li class="">24</li><li class="">25</li><li class="">26</li><li class="">27</li><li class="">28</li><li class="">29</li><li class="">30</li><li class="">31</li><li class="">32</li><li class="">33</li><li class="">34</li><li class="">35</li><li class="">36</li><li class="">37</li><li class="">38</li><li class="">39</li><li class="">40</li><li class="">41</li><li class="">42</li><li class="">43</li><li class="">44</li><li class="">45</li><li class="">46</li><li class="">47</li><li class="">48</li><li class="">49</li><li class="">50</li><li class="">51</li><li class="">52</li><li class="">53</li><li class="">54</li><li class="">55</li><li class="">56</li><li class="">57</li><li class="">58</li><li class="">59</li></ol></li></ul></div></div><div class="layui-laydate-footer"><div class="laydate-footer-btns">
		<span lay-type="clear" class="laydate-btns-clear fxmclear">清空</span>
		<span lay-type="now" class="laydate-btns-now fxmnow">现在</span>
		<span lay-type="confirm" class="laydate-btns-confirm fxmok" >确定</span></div></div></div>`;
		t.clickEvent();
	},
	clickEvent(){
		var t=this;
		$(t.boxid).on('click','input',function(e){
			console.log(111)
			e.stopPropagation()
			$('.layui-laydate').remove()
			$(this).parents(t.inputBox).append(t.str)
		})
		$(t.boxid).on('click','ol>li',function(){
			var box=$(this).parent('ol');
			if(box){
				box.find('li').removeClass('layui-this');
				$(this).addClass('layui-this')
			}
		})
		$(t.boxid).on('click','.fxmclear',function(){
			var bigbox= $(this).parents(t.inputBox);
			bigbox.find('input').val('')
			t.clear(bigbox);
		})
		$(t.boxid).on('click','.fxmnow',function(){
			var bigbox= $(this).parents(t.inputBox);
			var date = new Date();
			var hour = date.getHours();
			var minute = date.getMinutes();
			var second = date.getSeconds();
			var v=hour+':'+minute+':'+second
			bigbox.find('input').val(v)
			t.clear(bigbox);
		})
		$(t.boxid).on('click','.fxmok',function(){
			var bigbox= $(this).parents(t.inputBox);
			a=bigbox.find('ol').length;
			var hh= bigbox.find('ol').eq(0).find('.layui-this').html()
			var mm= bigbox.find('ol').eq(1).find('.layui-this').html()
			var ss= bigbox.find('ol').eq(2).find('.layui-this').html()
			bigbox.find('input').val(hh+":"+mm+":"+ss)
			t.clear(bigbox);
		})
		$(t.boxid).on('click','.layui-laydate',function(e){
			e.stopPropagation()
		})
		$('html').click(function(){
			$('.layui-laydate').remove()
		})
	},
	clear(bigbox){
		bigbox.find('.layui-laydate').remove()
	}

}
