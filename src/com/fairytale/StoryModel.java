package com.fairytale;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class StoryModel {
	private DatabaseAccessModel dbm;
	private Context context;
	//Story Data
	private int story_id;
	private String story_title = null;
	private Bitmap story_thumbnail = null;
	//Item Data
	private int count_item;
	private ArrayList<ItemModel> items;
	//Story database content
	private ContentValues story_record;
	
	public StoryModel(DatabaseAccessModel database, Context cont, int id){
		this.dbm = database;
		this.context = cont;
		this.story_id = id;
		
		this.story_record = new ContentValues();
		this.story_record.put("story_id", id);
	}
	public int getStoryID(){
		return this.story_id;
	}
	public String getTitle(){
		return this.story_title;
	}
	public StoryModel setTitle(String title){
		this.story_title = title;
		this.story_record.put("story_title", title);
		
		return this;
	}
	public Bitmap getThumbnail(){
		Bitmap img;
		if((img=this.story_thumbnail) == null){
			String[] select = new String[]{"STORY_ID"};
			String[] sel_arg = new String[]{""+story_id};
			byte[] blob;
			blob = dbm.getBlob("STORY_THUMB", "STORY", select, sel_arg);
			if(blob == null)
				img = null;
			else
				img = BitmapFactory.decodeByteArray(blob, 0, blob.length);
		}
		
		return img;
	}
	public StoryModel setThumbnail(Bitmap thumb){
		this.story_thumbnail = thumb;
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		thumb.compress(Bitmap.CompressFormat.PNG, 100, stream);
		
		byte[] blob_thumb = stream.toByteArray();
		this.story_record.put("story_thumb", blob_thumb);
		
		return this;
	}
	public StoryModel initialItem(int num){
		this.count_item = num;
		this.items = new ArrayList<ItemModel>();
		for(int item_loop = 0; item_loop<num;item_loop++){
			ItemModel item = new ItemModel(this.story_id, item_loop);
			this.items.add(item);
		}
		
		return this;
	}
	public int numberOfItem(){
		return this.count_item;
	}
	public ItemModel getItem(int index){
		return this.items.get(index);
	}
	
	public void generateFromDB(){
		byte[] blob = null;
		
		blob = dbm.getBlob("STORY_THUMB", "STORY", new String[]{"STORY_ID"}, new String[]{""+this.story_id});
		Bitmap thumb;
		if(blob == null)
			thumb = null;
		else 
			thumb = BitmapFactory.decodeByteArray(blob, 0, blob.length);
		this.story_thumbnail = thumb;
		
		String title = dbm.getString("STORY_TITLE", "STORY", new String[]{"STORY_ID"}, new String[]{""+this.story_id});
		this.story_title = title;
		
		ArrayList<HashMap<String,Integer>> list = dbm.selectQuery("ITEM", new String[]{"ITEM_ID"}, "STORY_ID=?", new String[]{""+this.story_id}, null, null, "ITEM_ID asc");
		this.count_item = list.size();
		this.items = new ArrayList<ItemModel>();
		for(int item_loop = 0; item_loop<this.count_item; item_loop++){
			ItemModel item = new ItemModel(this.story_id, list.get(item_loop).get("ITEM_ID"));
			item.generateFromDB();
			this.items.add(item);
		}
	}
	public void insertToDatabase(){
		dbm.insertContentValue("story", this.story_record);
		for(int item_loop=0; item_loop<this.items.size();item_loop++){
			items.get(item_loop).insertToDatabase();
		}
	}
	//Item Model
	public class ItemModel {
		//Item Data
		private int story_id;
		private int item_id;
		private String item_type = null;
		//Content Data
		private int count_img;
		private ArrayList<ImageModel> images;
		private int count_sound;
		private ArrayList<SoundModel> sounds;
		private int count_txt;
		private ArrayList<TextModel> texts;
		private int count_bgm;
		private ArrayList<BgmModel> bgms;
		//Item database content
		private ContentValues item_record;
		
		public ItemModel(int parent_id,int id){
			this.story_id = parent_id;
			this.item_id = id;
			
			this.item_record = new ContentValues();
			this.item_record.put("story_id", parent_id);
			this.item_record.put("item_id", id);
		}
		
		public int getID(){
			return this.item_id;
		}
		public String getType(){
			return this.item_type;
		}
		public ItemModel setType(String type){
			this.item_type = type;
			this.item_record.put("item_type", type);
			
			return this;
		}
		public ItemModel initialItem(int img_num, int sound_num, int txt_num, int bgm_num){
			//Initialize images data
			this.count_img = img_num;
			this.images = new ArrayList<ImageModel>();
			for(int img_loop = 0; img_loop<img_num;img_loop++){
				ImageModel image = new ImageModel(this.story_id, this.item_id, img_loop);
				this.images.add(image);
			}
		    //Initialize sounds data
		    this.count_sound = sound_num;
		    this.sounds = new ArrayList<SoundModel>();
		    SoundModel sound = new SoundModel(this.story_id, this.item_id);
		    this.sounds.add(sound);
		    //Initialize texts data
		    this.count_txt = txt_num;
		    this.texts = new ArrayList<TextModel>();
		    for(int txt_loop = 0; txt_loop<txt_num;txt_loop++){
			    TextModel text = new TextModel(this.story_id, this.item_id, txt_loop);
			    this.texts.add(text);
			}
			//Initialize bgms data
			this.count_bgm = bgm_num;
			this.bgms = new ArrayList<BgmModel>();
			BgmModel bgm = new BgmModel(this.story_id, this.item_id);
			this.bgms.add(bgm);
			
			return this;
		}
		public int numberOfImage(){
			return this.count_img;
		}
		public ImageModel getImage(int index){
			return this.images.get(index);
		}
		public int numberOfSound(){
			return this.count_sound;
		}
		public SoundModel getSound(int index){
			return this.sounds.get(index);
		}
		public int numberOfText(){
			return this.count_txt;
		}
		public TextModel getText(int index){
			return this.texts.get(index);
		}
		public int numberOfBgm(){
			return this.count_bgm;
		}
		public BgmModel getBgm(int index){
			return this.bgms.get(index);
		}
		
		public void generateFromDB() {
			ArrayList<HashMap<String,Integer>> list;
			
			String type = dbm.getString("ITEM_TYPE", "ITEM", new String[]{"STORY_ID","ITEM_ID"}, new String[]{""+this.story_id,""+this.item_id});
			this.item_type = type;
			
			list = dbm.selectQuery("IMAGE", new String[]{"IMAGE_NUM"}, "STORY_ID=? AND ITEM_ID=?", new String[]{""+this.story_id,""+this.item_id}, null, null, "IMAGE_NUM asc");
			this.count_img = list.size();
			this.images = new ArrayList<ImageModel>();
			for(int img_loop = 0; img_loop<this.count_img; img_loop++){
				ImageModel img = new ImageModel(this.story_id, this.item_id, list.get(img_loop).get("IMAGE_NUM"));
				img.generateFromDB();
				this.images.add(img);
			}
			
			this.count_sound = 1;
			this.sounds = new ArrayList<SoundModel>();
			SoundModel sound = new SoundModel(this.story_id, this.item_id);
			sound.generateFromDB();
			sounds.add(sound);
			
			list = dbm.selectQuery("TEXT", new String[]{"TEXT_NUM"}, "STORY_ID=? AND ITEM_ID=?", new String[]{""+this.story_id,""+this.item_id}, null, null, "TEXT_NUM asc");
			this.count_txt = list.size();
			this.texts = new ArrayList<TextModel>();
			for(int txt_loop = 0; txt_loop<this.count_txt; txt_loop++){
				TextModel txt = new TextModel(this.story_id, this.item_id, list.get(txt_loop).get("TEXT_NUM"));
				txt.generateFromDB();
				this.texts.add(txt);
			}
			
			list = dbm.selectQuery("BGM", new String[]{"BGM_NUM"}, "STORY_ID=? AND ITEM_ID=?", new String[]{""+this.story_id,""+this.item_id}, null, null, "BGM_NUM asc");
			this.count_bgm = list.size();
			this.bgms = new ArrayList<BgmModel>();
			for(int bgm_loop = 0; bgm_loop<this.count_bgm; bgm_loop++){
				BgmModel bgm = new BgmModel(this.story_id, this.item_id);
				bgm.generateFromDB();
				this.bgms.add(bgm);
			}
		}
		public void insertToDatabase(){
			dbm.insertContentValue("item", this.item_record);
			
			for(int img_loop=0; img_loop<this.images.size();img_loop++){
				this.images.get(img_loop).insertToDatabase();
			}
			for(int sound_loop=0; sound_loop<this.sounds.size();sound_loop++){
				this.sounds.get(sound_loop).insertToDatabase();
			}
			for(int txt_loop=0; txt_loop<this.texts.size();txt_loop++){
				this.texts.get(txt_loop).insertToDatabase();
			}
			for(int bgm_loop=0; bgm_loop<this.bgms.size();bgm_loop++){
				this.bgms.get(bgm_loop).insertToDatabase();
			}
		}
		
		//Image Model
		public class ImageModel{
			//Image data
			private int story_id;
			private int item_id;
			private int image_id;
			private Bitmap image = null;
			
			//Image database content
			private ContentValues image_record;
			
			public ImageModel(int story_id, int item_id, int id){
				this.story_id = story_id;
				this.item_id = item_id;
				this.image_id = id;
				
				this.image_record = new ContentValues();
				this.image_record.put("story_id", story_id);
				this.image_record.put("item_id", item_id);
				this.image_record.put("image_num", id);
			}
			public int getID(){
				return this.image_id;
			}
			public Bitmap getImage(){
				Bitmap img;
				if((img=this.image) == null){
					String[] select = new String[]{"STORY_ID","ITEM_ID","IMAGE_NUM"};
					String[] sel_arg = new String[]{""+this.story_id,""+this.item_id,""+this.image_id};
					byte[] blob = dbm.getBlob("IMAGE", "IMAGE", select, sel_arg);
					img = BitmapFactory.decodeByteArray(blob, 0, blob.length);
				}
				return img;
			}
			public ImageModel setImage(Bitmap image){
				this.image = image;
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				image.compress(Bitmap.CompressFormat.PNG, 100, stream);
				
				byte[] blob_img = stream.toByteArray();
				this.image_record.put("image", blob_img);
				
				return this;
			}
			public void generateFromDB(){
				byte[] blob = dbm.getBlob("IMAGE", "IMAGE", new String[]{"STORY_ID","ITEM_ID","IMAGE_NUM"}, new String[]{""+this.story_id,""+this.item_id,""+this.image_id});
				Bitmap image = BitmapFactory.decodeByteArray(blob, 0, blob.length);
				this.setImage(image);
			}
			public void insertToDatabase(){
				dbm.insertContentValue("image", this.image_record);
			}
		}
		//Sound Model
		public class SoundModel{
			//Sound data
			private int story_id;
			private int item_id;
			private File sound = null; // 데이터베이스에 들어갈 오디오 소스로 변경
	      
			//Sound database content
			private ContentValues sound_record;
	      
			public SoundModel(int story_id, int item_id){
				this.story_id = story_id;
				this.item_id = item_id;
	        
				this.sound_record = new ContentValues();
				this.sound_record.put("story_id", story_id);
				this.sound_record.put("item_id", item_id);
			}
			
			public File getAudio(){
				File audio;
				if((audio = this.sound) == null){
					String[] select = new String[]{"STORY_ID","ITEM_ID"};
					String[] sel_arg = new String[]{""+this.story_id,""+this.item_id};
					byte[] blob = dbm.getBlob("SOUND", "SOUND", select, sel_arg);
					audio = Converter.byteToFile(blob, context);
				}
				return audio;
			}
			public SoundModel setAudio(File audio){
				this.sound = audio;
				
				byte[] blob_audio = Converter.fileToByte(audio);
				this.sound_record.put("sound", blob_audio);
				
				return this;
			}
			
			public void generateFromDB(){
				byte[] blob = dbm.getBlob("SOUND", "SOUND", new String[]{"STORY_ID","ITEM_ID"}, new String[]{""+this.story_id,""+this.item_id});
				File audio = Converter.byteToFile(blob, context);
				this.setAudio(audio);
			}
			public void insertToDatabase(){
				dbm.insertContentValue("sound", this.sound_record);
			}
		}
		//Text Model
		public class TextModel{
			//Image data
			private int story_id;
			private int item_id;
			private int text_id;
			private String text = null;
			private Integer text_time = null;
			
			//Image database content
			private ContentValues text_record;
			
			public TextModel(int story_id, int item_id, int id){
				this.story_id = story_id;
				this.item_id = item_id;
				this.text_id = id;
				
				this.text_record = new ContentValues();
				this.text_record.put("story_id", story_id);
				this.text_record.put("item_id", item_id);
				this.text_record.put("text_num", id);
			}
			public int getID(){
				return this.text_id;
			}
			public String getText(){
				String text;
				if((text = this.text) == null){
					String[] select = new String[]{"STORY_ID","ITEM_ID","TEXT_NUM"};
					String[] sel_arg = new String[]{""+this.story_id,""+this.item_id,""+this.text_id};
					text = dbm.getString("TEXT_STR", "TEXT", select, sel_arg);
				}
				return text;
			}
			public TextModel setText(String text){
				this.text = text;
				this.text_record.put("text_str", text);
				
				return this;
			}
			public int getTextTime(){
				Integer time;
				if((time = this.text_time) == null){
					String[] select = new String[]{"STORY_ID","ITEM_ID","TEXT_NUM"};
					String[] sel_arg = new String[]{""+this.story_id,""+this.item_id,""+this.text_id};
					time = dbm.getInt("TEXT_TIME", "TEXT", select, sel_arg);
				}
				return time.intValue();
			}
			public TextModel setTextTime(int time){
				this.text_time = time;
				this.text_record.put("text_time", time);
				
				return this;
			}
			
			public void generateFromDB(){
				String text = dbm.getString("TEXT_STR", "TEXT", new String[]{"STORY_ID","ITEM_ID","TEXT_NUM"}, new String[]{""+this.story_id,""+this.item_id,""+this.text_id});
				this.setText(text);
				
				int time = dbm.getInt("TEXT_TIME", "TEXT", new String[]{"STORY_ID","ITEM_ID","TEXT_NUM"}, new String[]{""+this.story_id,""+this.item_id,""+this.text_id});
				this.setTextTime(time);
			}
			public void insertToDatabase(){
				dbm.insertContentValue("text", this.text_record);
			}
		}
		//BGM Model
		public class BgmModel{
			//Image data
			private int story_id;
			private int item_id;
			private File bgm = null;
			
			//Image database content
			private ContentValues bgm_record;
			
			public BgmModel(int story_id, int item_id){
				this.story_id = story_id;
				this.item_id = item_id;
				
				this.bgm_record = new ContentValues();
				this.bgm_record.put("story_id", story_id);
				this.bgm_record.put("item_id", item_id);
			}
			public File getAudio(){
				File audio;
				if((audio = this.bgm) == null){
					String[] select = new String[]{"STORY_ID","ITEM_ID"};
					String[] sel_arg = new String[]{""+this.story_id,""+this.item_id};
					byte[] blob = dbm.getBlob("BGM", "BGM", select, sel_arg);
					audio = Converter.byteToFile(blob, context);
				}
				return audio;
			}
			public BgmModel setAudio(File audio){
				this.bgm = audio;
				
				byte[] blob_audio = Converter.fileToByte(audio);
				this.bgm_record.put("bgm", blob_audio);
				
				return this;
			}
			
			public void generateFromDB(){
				byte[] blob = dbm.getBlob("BGM", "BGM", new String[]{"STORY_ID","ITEM_ID"}, new String[]{""+this.story_id,""+this.item_id});
				File audio = Converter.byteToFile(blob, context);
				this.setAudio(audio);
			}
			public void insertToDatabase(){
				dbm.insertContentValue("bgm", this.bgm_record);
			}
		}
	}
}