/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.selagroup.schedu.HorizontalPanelSwitcher;
import com.selagroup.schedu.HorizontalPanelSwitcher.OnPanelSwitchListener;
import com.selagroup.schedu.ObservableScrollView;
import com.selagroup.schedu.ObservableScrollView.ScrollViewListener;
import com.selagroup.schedu.R;
import com.selagroup.schedu.ScheduApplication;
import com.selagroup.schedu.managers.CourseManager;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Term;
import com.selagroup.schedu.model.TimePlaceBlock;

/**
 * The Class CalendarActivity.
 */
public class CalendarActivity extends Activity {
	private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("EEEE, MMM d, yyyy");
	private static final int DAYS_IN_WEEK = 7;
	private static final int DAY_VIEW_BUFFER = 15;
	private static final LinearLayout.LayoutParams WEEK_LAYOUT_PARAMS;
	static {
		WEEK_LAYOUT_PARAMS = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		WEEK_LAYOUT_PARAMS.setMargins(0, 2, 0, 2);
	}
	private static final RelativeLayout.LayoutParams DAY_PARENT_LAYOUT_PARAMS;
	static {
		DAY_PARENT_LAYOUT_PARAMS = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
	}

	// Widgets
	private ScrollView calendar_sv_week;

	private ToggleButton calendar_btn_day;
	private ToggleButton calendar_btn_week;

	// Day widgets
	private HorizontalPanelSwitcher calendar_switcher_day;

	private View mThisDayView;
	private RelativeLayout mThisDayParentView;
	private RelativeLayout mThisDayCourses;
	private ObservableScrollView mThisDayScroll;
	private TextView mThisDayDate;
	private LinkedList<TextView> mThisDayCourseBlocks = new LinkedList<TextView>();

	private View mNextDayView;
	private RelativeLayout mNextDayParentView;
	private RelativeLayout mNextDayCourses;
	private ObservableScrollView mNextDayScroll;
	private TextView mNextDayDate;
	private LinkedList<TextView> mNextDayCourseBlocks = new LinkedList<TextView>();

	private View mPrevDayView;
	private RelativeLayout mPrevDayParentView;
	private RelativeLayout mPrevDayCourses;
	private ObservableScrollView mPrevDayScroll;
	private TextView mPrevDayDate;
	private LinkedList<TextView> mPrevDayCourseBlocks = new LinkedList<TextView>();

	// Week widgets
	private LinearLayout calendar_week_courses;
	private ArrayList<TextView> mWeekDayBlocks = new ArrayList<TextView>(7);

	// Managers
	private CourseManager mCourseManager;

	// Data
	private List<Course> mCourses;
	private Calendar mThisDay;
	private Calendar mNextDay;
	private Calendar mPrevDay;
	private Term mCurrentTerm;
	private float mDensity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		ScheduApplication myApp = ((ScheduApplication) getApplication());
		mCourseManager = myApp.getCourseManager();

		// Set up the correct day to view
		mThisDay = Calendar.getInstance();
		mCurrentTerm = myApp.getCurrentTerm();
		if (mThisDay.after(mCurrentTerm.getEndDate())) {
			mThisDay = mCurrentTerm.getEndDate();
		}
		else if (mThisDay.before(mCurrentTerm.getStartDate())) {
			mThisDay = mCurrentTerm.getStartDate();
		}
		mNextDay = (Calendar) mThisDay.clone();
		mNextDay.roll(Calendar.DAY_OF_MONTH, true);
		mPrevDay = (Calendar) mThisDay.clone();
		mPrevDay.roll(Calendar.DAY_OF_MONTH, false);

		mDensity = getResources().getDisplayMetrics().density;

		// Get all courses for the current term
		mCourses = mCourseManager.getAllForTerm(mCurrentTerm.getID());

		initWidgets();
		initListeners();
		initDayView();
		initWeekView();
	}

	/**
	 * Initializes the widgets.
	 */
	private void initWidgets() {

		calendar_btn_day = (ToggleButton) findViewById(R.id.calendar_btn_day);
		calendar_btn_day.setChecked(true);
		calendar_btn_week = (ToggleButton) findViewById(R.id.calendar_btn_week);

		// Construct the day calendar view
		calendar_switcher_day = (HorizontalPanelSwitcher) findViewById(R.id.calendar_switcher_day);

		mThisDayParentView = new RelativeLayout(this);
		mThisDayParentView.setLayoutParams(DAY_PARENT_LAYOUT_PARAMS);

		mPrevDayParentView = new RelativeLayout(this);
		mPrevDayParentView.setLayoutParams(DAY_PARENT_LAYOUT_PARAMS);

		mNextDayParentView = new RelativeLayout(this);
		mNextDayParentView.setLayoutParams(DAY_PARENT_LAYOUT_PARAMS);

		calendar_switcher_day.setCenterLayout(mThisDayParentView);
		calendar_switcher_day.setLeftLayout(mPrevDayParentView);
		calendar_switcher_day.setRightLayout(mNextDayParentView);

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		// Initialize this day
		mThisDayView = inflater.inflate(R.layout.layout_day, mThisDayParentView);
		mThisDayCourses = (RelativeLayout) mThisDayView.findViewById(R.id.calendar_day_courses);
		mThisDayScroll = (ObservableScrollView) mThisDayView.findViewById(R.id.calendar_sv_day);
		mThisDayDate = (TextView) mThisDayView.findViewById(R.id.calendar_tv_date);
		mThisDayDate.setText(DAY_FORMAT.format(mThisDay.getTime()));

		// Initialize next day
		mNextDayView = inflater.inflate(R.layout.layout_day, mNextDayParentView);
		mNextDayCourses = (RelativeLayout) mNextDayView.findViewById(R.id.calendar_day_courses);
		mNextDayScroll = (ObservableScrollView) mNextDayView.findViewById(R.id.calendar_sv_day);
		mNextDayDate = (TextView) mNextDayView.findViewById(R.id.calendar_tv_date);
		mNextDayDate.setText(DAY_FORMAT.format(mNextDay.getTime()));

		// Initialize previous day
		mPrevDayView = inflater.inflate(R.layout.layout_day, mPrevDayParentView);
		mPrevDayCourses = (RelativeLayout) mPrevDayView.findViewById(R.id.calendar_day_courses);
		mPrevDayScroll = (ObservableScrollView) mPrevDayView.findViewById(R.id.calendar_sv_day);
		mPrevDayDate = (TextView) mPrevDayView.findViewById(R.id.calendar_tv_date);
		mPrevDayDate.setText(DAY_FORMAT.format(mPrevDay.getTime()));

		// Week stuff
		calendar_week_courses = (LinearLayout) findViewById(R.id.calendar_week_courses);
		calendar_sv_week = (ScrollView) findViewById(R.id.calendar_sv_week);

		// Scroll to the current time in the day
		mThisDayScroll.post(new Runnable() {
			public void run() {
				mThisDayScroll.scrollTo(0, (int) (mDensity * (60 * mThisDay.get(Calendar.HOUR_OF_DAY) + mThisDay.get(Calendar.MINUTE))));
			}
		});
	}

	/**
	 * Initializes the listeners.
	 */
	private void initListeners() {
		OnClickListener buttonListener = new OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.calendar_btn_day:
					mThisDayDate.setText(DAY_FORMAT.format(mThisDay.getTime()));
					calendar_btn_day.setChecked(true);
					calendar_btn_week.setChecked(false);
					calendar_switcher_day.setVisibility(View.VISIBLE);
					calendar_sv_week.setVisibility(View.GONE);
					break;
				case R.id.calendar_btn_week:
					calendar_btn_day.setChecked(false);
					calendar_btn_week.setChecked(true);
					calendar_switcher_day.setVisibility(View.GONE);
					calendar_sv_week.setVisibility(View.VISIBLE);
					break;
				}
			}
		};

		calendar_btn_day.setOnClickListener(buttonListener);
		calendar_btn_week.setOnClickListener(buttonListener);

		calendar_switcher_day.setOnPanelSwitchListener(new OnPanelSwitchListener() {
			public void switchRight() {
				mThisDay.roll(Calendar.DAY_OF_MONTH, true);
				mNextDay.roll(Calendar.DAY_OF_MONTH, true);
				mPrevDay.roll(Calendar.DAY_OF_MONTH, true);

				mThisDayDate.setText(DAY_FORMAT.format(mThisDay.getTime()));
				mNextDayDate.setText(DAY_FORMAT.format(mNextDay.getTime()));
				mPrevDayDate.setText(DAY_FORMAT.format(mPrevDay.getTime()));
				initDayView();
			}

			public void switchLeft() {
				mThisDay.roll(Calendar.DAY_OF_MONTH, false);
				mNextDay.roll(Calendar.DAY_OF_MONTH, false);
				mPrevDay.roll(Calendar.DAY_OF_MONTH, false);

				mThisDayDate.setText(DAY_FORMAT.format(mThisDay.getTime()));
				mNextDayDate.setText(DAY_FORMAT.format(mNextDay.getTime()));
				mPrevDayDate.setText(DAY_FORMAT.format(mPrevDay.getTime()));
				initDayView();
			}
		});
		
		mThisDayScroll.setOnScrollListener(new ScrollViewListener() {
			public void onScrollChanged(ObservableScrollView view, int x, int y, int oldx, int oldy) {
				mNextDayScroll.scrollTo(0, y);
				mPrevDayScroll.scrollTo(0, y);
			}
		});
	}

	private void initDayView() {
		// Remove any existing day views
		mThisDayCourses.removeAllViews();
		mNextDayCourses.removeAllViews();
		mPrevDayCourses.removeAllViews();

		mPrevDayScroll.post(new Runnable() {
			public void run() {
				mPrevDayScroll.scrollTo(0, (int) mThisDayScroll.getScrollY());
			}
		});

		mNextDayScroll.post(new Runnable() {
			public void run() {
				mNextDayScroll.scrollTo(0, (int) mThisDayScroll.getScrollY());
			}
		});

		// Add courses for current day
		for (Course course : mCourses) {
			List<TimePlaceBlock> blocks = course.getBlocksOnDay(mThisDay.get(Calendar.DAY_OF_WEEK) - 1);
			for (TimePlaceBlock block : blocks) {
				addCourseBlockToDay(course, block, mThisDay, mThisDayCourseBlocks, mThisDayCourses);
			}
		}

		// Add courses for next day
		for (Course course : mCourses) {
			List<TimePlaceBlock> blocks = course.getBlocksOnDay(mNextDay.get(Calendar.DAY_OF_WEEK) - 1);
			for (TimePlaceBlock block : blocks) {
				addCourseBlockToDay(course, block, mNextDay, mNextDayCourseBlocks, mNextDayCourses);
			}
		}

		// Add courses for previous day
		for (Course course : mCourses) {
			List<TimePlaceBlock> blocks = course.getBlocksOnDay(mPrevDay.get(Calendar.DAY_OF_WEEK) - 1);
			for (TimePlaceBlock block : blocks) {
				addCourseBlockToDay(course, block, mPrevDay, mPrevDayCourseBlocks, mPrevDayCourses);
			}
		}
	}

	private void initWeekView() {
		// Remove any existing week views
		calendar_week_courses.removeAllViews();

		// Tree map to store sorted blocks and the associated courses
		TreeMap<TimePlaceBlock, Course> courseBlocks = new TreeMap<TimePlaceBlock, Course>();

		// Set the day to the first day of the current week
		TextView weekDayBlock;
		Calendar day = (Calendar) mThisDay.clone();
		day.set(Calendar.DAY_OF_WEEK, mThisDay.getFirstDayOfWeek());

		// Add blocks for each day
		for (int i = 0; i < DAYS_IN_WEEK; ++i) {
			// Initialize the day header block
			weekDayBlock = new TextView(this);
			weekDayBlock.setTextColor(Color.BLACK);
			weekDayBlock.setBackgroundColor(Color.LTGRAY);
			weekDayBlock.setText(DAY_FORMAT.format(day.getTime()));
			weekDayBlock.setBackgroundResource(R.drawable.calendar_block);
			weekDayBlock.setLayoutParams(WEEK_LAYOUT_PARAMS);

			// Add the week's day block to the list and the linear layout
			mWeekDayBlocks.add(weekDayBlock);
			calendar_week_courses.addView(weekDayBlock);

			// Add time blocks to tree map for sorting by time of day
			for (Course course : mCourses) {
				List<TimePlaceBlock> blocks = course.getBlocksOnDay(i);
				for (TimePlaceBlock block : blocks) {
					courseBlocks.put(block, course);
				}
			}

			// Add time blocks to the view in chronological order
			for (Entry<TimePlaceBlock, Course> entry : courseBlocks.entrySet()) {
				addCourseBlockToWeek(entry.getValue(), entry.getKey(), day);
			}

			courseBlocks.clear();

			// Increment the day by 1
			day.add(Calendar.DAY_OF_WEEK, 1);
		}
	}

	private void addCourseBlockToDay(Course iCourse, TimePlaceBlock iBlock, Calendar iDay, LinkedList<TextView> iBlockList, RelativeLayout iLayout) {

		// Initialize the block's colors, text, and listeners
		TextView courseDayBlock = getCourseBlock(iCourse, iBlock);

		// Sets the block height appropriately (1 minute = 1 dp)
		int blockHeight_dp = (int) (iBlock.getMinutesElapsed() * mDensity + 0.5f);

		// Sets the block distance from the top of the layout
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, blockHeight_dp);
		params.setMargins(0, (int) ((iBlock.getMinutesAfterMidnight() + DAY_VIEW_BUFFER) * mDensity + 0.5f), 0, 0);
		courseDayBlock.setLayoutParams(params);
		courseDayBlock.setTag(iDay.clone());

		// Add the block to the list of blocks and the layout
		iBlockList.add(courseDayBlock);
		iLayout.addView(courseDayBlock);
	}

	private void addCourseBlockToWeek(Course iCourse, TimePlaceBlock iBlock, Calendar iDay) {
		// Initialize the block's colors, text, and listeners
		TextView weekDayBlock = getCourseBlock(iCourse, iBlock);
		weekDayBlock.setTag(iDay.clone());

		weekDayBlock.setLayoutParams(WEEK_LAYOUT_PARAMS);

		// Add the week's day block to the list and the linear layout
		mWeekDayBlocks.add(weekDayBlock);
		calendar_week_courses.addView(weekDayBlock);
	}

	private TextView getCourseBlock(Course iCourse, TimePlaceBlock iBlock) {
		TextView courseBlock = new TextView(this);
		courseBlock.setTextColor(Color.BLACK);
		courseBlock.setBackgroundColor(Color.GREEN);
		courseBlock.setText(iCourse.getCourseName() + " (" + iCourse.getCourseCode() + ")\n" + iBlock.getLocation());
		courseBlock.setBackgroundResource(R.drawable.course_block_low);
		courseBlock.setClickable(true);
		courseBlock.setOnClickListener(new CourseClickListener(iCourse.getID(), iBlock.getID()));
		return courseBlock;
	}

	private class CourseClickListener implements OnClickListener {
		private int mCourseID;
		private int mBlockID;

		public CourseClickListener(int iCourseID, int iBlockID) {
			mCourseID = iCourseID;
			mBlockID = iBlockID;
		}

		public void onClick(View view) {
			Intent showCourse = new Intent(CalendarActivity.this, CourseActivity.class);
			showCourse.putExtra("courseID", mCourseID);
			showCourse.putExtra("blockID", mBlockID);
			showCourse.putExtra("day", (Calendar) view.getTag());
			startActivity(showCourse);
		}
	}
}
