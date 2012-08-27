/**
 * @author Nick Huebner and Mark Redden
 * @version 1.0
 */

package com.selagroup.schedu.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.selagroup.schedu.HorizontalPanelSwitcher;
import com.selagroup.schedu.HorizontalPanelSwitcher.OnDragListener;
import com.selagroup.schedu.HorizontalPanelSwitcher.OnPanelSwitchListener;
import com.selagroup.schedu.ObservableScrollView;
import com.selagroup.schedu.ObservableScrollView.ScrollViewListener;
import com.selagroup.schedu.R;
import com.selagroup.schedu.ScheduApplication;
import com.selagroup.schedu.Utility;
import com.selagroup.schedu.managers.CourseManager;
import com.selagroup.schedu.managers.ExamManager;
import com.selagroup.schedu.model.ContentValueItem;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Exam;
import com.selagroup.schedu.model.Term;
import com.selagroup.schedu.model.TimePlaceBlock;

/**
 * The Class CalendarActivity.
 */
public class CalendarActivity extends Activity {
	private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("EEEE, MMM d");
	private static final SimpleDateFormat WEEK_FORMAT = new SimpleDateFormat("MMM d, yyyy");
	private static final int DAYS_IN_WEEK = 7;
	private static final int DAY_VIEW_BUFFER = 15;

	private static final LinearLayout.LayoutParams WEEK_LAYOUT_PARAMS;
	private static final RelativeLayout.LayoutParams DAY_PARENT_LAYOUT_PARAMS;

	private static final int ANIMATION_TIME = 200;
	private static final AlphaAnimation FADE_IN_ANIMATION;
	private static final AlphaAnimation FADE_OUT_ANIMATION;

	// Initialize constants
	static {
		WEEK_LAYOUT_PARAMS = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		WEEK_LAYOUT_PARAMS.setMargins(0, 2, 0, 2);

		DAY_PARENT_LAYOUT_PARAMS = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

		FADE_IN_ANIMATION = new AlphaAnimation(0, 1);
		FADE_IN_ANIMATION.setDuration(ANIMATION_TIME);
		FADE_IN_ANIMATION.setInterpolator(new LinearInterpolator());

		FADE_OUT_ANIMATION = new AlphaAnimation(1, 0);
		FADE_OUT_ANIMATION.setDuration(ANIMATION_TIME);
		FADE_OUT_ANIMATION.setInterpolator(new LinearInterpolator());
	}
	
	private static final Comparator<TimePlaceBlock> TIME_COMPARATOR = new Comparator<TimePlaceBlock>() {
		public int compare(TimePlaceBlock lhs, TimePlaceBlock rhs) {
			return lhs.getMinutesAfterMidnight() - rhs.getMinutesAfterMidnight();
		}
	};

	// Widgets
	private ToggleButton calendar_btn_day;
	private ToggleButton calendar_btn_week;
	private TextView calendar_tv_date;
	private ImageView calendar_btn_next;
	private ImageView calendar_btn_prev;

	// Day widgets
	private HorizontalPanelSwitcher calendar_switcher_day;
	private View mCurrentTimeLine;
	private RelativeLayout.LayoutParams mCurrentTimeLineParams;

	private View mThisDayView;
	private RelativeLayout mThisDayParentView;
	private RelativeLayout mThisDayCourses;
	private ObservableScrollView mThisDayScroll;
	private LinkedList<TextView> mThisDayCourseBlocks = new LinkedList<TextView>();

	private View mNextDayView;
	private RelativeLayout mNextDayParentView;
	private RelativeLayout mNextDayCourses;
	private ObservableScrollView mNextDayScroll;
	private LinkedList<TextView> mNextDayCourseBlocks = new LinkedList<TextView>();

	private View mPrevDayView;
	private RelativeLayout mPrevDayParentView;
	private RelativeLayout mPrevDayCourses;
	private ObservableScrollView mPrevDayScroll;
	private LinkedList<TextView> mPrevDayCourseBlocks = new LinkedList<TextView>();

	// Week widgets
	private HorizontalPanelSwitcher calendar_switcher_week;

	private View mThisWeekView;
	private RelativeLayout mThisWeekParentView;
	private LinearLayout mThisWeekLayout;
	private ObservableScrollView mThisWeekScroll;
	private LinkedList<TextView> mThisWeekCourseBlocks = new LinkedList<TextView>();

	private View mNextWeekView;
	private RelativeLayout mNextWeekParentView;
	private LinearLayout mNextWeekLayout;
	private ObservableScrollView mNextWeekScroll;
	private LinkedList<TextView> mNextWeekCourseBlocks = new LinkedList<TextView>();

	private View mPrevWeekView;
	private RelativeLayout mPrevWeekParentView;
	private LinearLayout mPrevWeekLayout;
	private ObservableScrollView mPrevWeekScroll;
	private LinkedList<TextView> mPrevWeekCourseBlocks = new LinkedList<TextView>();

	// Managers
	private CourseManager mCourseManager;
	private ExamManager mExamManager;

	// Data
	private List<Course> mCourses;
	private List<Exam> mExams = new ArrayList<Exam>();
	private Calendar mThisDay;
	private Calendar mNextDay;
	private Calendar mPrevDay;
	private Calendar mThisWeek;
	private Calendar mNextWeek;
	private Calendar mPrevWeek;
	private Term mCurrentTerm;
	private float mDensity;
	private boolean mScrollLock = false;
	private boolean mDayMode = true;

	private interface StartFadeInListener {
		public void onStartFadeIn();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		ScheduApplication myApp = ((ScheduApplication) getApplication());
		mCourseManager = myApp.getCourseManager();
		mExamManager = myApp.getExamManager();

		// Set up the correct day to view
		mThisDay = Calendar.getInstance();
		mCurrentTerm = myApp.getCurrentTerm();
		if (mThisDay.after(mCurrentTerm.getEndDate())) {
			mThisDay = mCurrentTerm.getEndDate();
		} else if (mThisDay.before(mCurrentTerm.getStartDate())) {
			mThisDay = mCurrentTerm.getStartDate();
		}
		mNextDay = (Calendar) mThisDay.clone();
		mNextDay.roll(Calendar.DAY_OF_MONTH, true);
		mPrevDay = (Calendar) mThisDay.clone();
		mPrevDay.roll(Calendar.DAY_OF_MONTH, false);

		mThisWeek = (Calendar) mThisDay.clone();
		mThisWeek.set(Calendar.DAY_OF_WEEK, mThisDay.getFirstDayOfWeek());
		mNextWeek = (Calendar) mThisWeek.clone();
		mNextWeek.add(Calendar.WEEK_OF_YEAR, 1);
		mPrevWeek = (Calendar) mThisWeek.clone();
		mPrevWeek.add(Calendar.WEEK_OF_YEAR, -1);

		mDensity = getResources().getDisplayMetrics().density;

		// Get all courses for the current term
		mCourses = mCourseManager.getAllForTerm(mCurrentTerm.getID());

		// Get all exams for these courses
		for (Course course : mCourses) {
			mExams.addAll(mExamManager.getAllForCourse(course.getID()));
		}

		initWidgets();
		initListeners();
		initDayView();
		showDayView();
	}

	/**
	 * Context menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Utility.buildOptionsMenu(CalendarActivity.this, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (Utility.handleOptionsMenuSelection(CalendarActivity.this, item)) {
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Initializes the widgets.
	 */
	private void initWidgets() {
		initDayWidgets();
		initWeekWidgets();
	}

	/**
	 * Initializes widgets for the day view
	 */
	private void initDayWidgets() {
		calendar_btn_day = (ToggleButton) findViewById(R.id.calendar_btn_day);
		calendar_btn_day.setChecked(true);
		calendar_btn_week = (ToggleButton) findViewById(R.id.calendar_btn_week);

		calendar_btn_next = (ImageView) findViewById(R.id.calendar_btn_next);
		calendar_btn_prev = (ImageView) findViewById(R.id.calendar_btn_prev);
		calendar_tv_date = (TextView) findViewById(R.id.calendar_tv_date);

		calendar_tv_date.setText(DAY_FORMAT.format(mThisDay.getTime()));

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

		// Initialize next day
		mNextDayView = inflater.inflate(R.layout.layout_day, mNextDayParentView);
		mNextDayCourses = (RelativeLayout) mNextDayView.findViewById(R.id.calendar_day_courses);
		mNextDayScroll = (ObservableScrollView) mNextDayView.findViewById(R.id.calendar_sv_day);

		// Initialize previous day
		mPrevDayView = inflater.inflate(R.layout.layout_day, mPrevDayParentView);
		mPrevDayCourses = (RelativeLayout) mPrevDayView.findViewById(R.id.calendar_day_courses);
		mPrevDayScroll = (ObservableScrollView) mPrevDayView.findViewById(R.id.calendar_sv_day);

		// Scroll to the current time in the day
		mThisDayScroll.post(new Runnable() {
			public void run() {
				mThisDayScroll.scrollTo(0, (int) (mDensity * (60 * mThisDay.get(Calendar.HOUR_OF_DAY) + mThisDay.get(Calendar.MINUTE))));
			}
		});
	}

	/**
	 * Initializes widgets for the week view
	 */
	private void initWeekWidgets() {
		calendar_btn_week = (ToggleButton) findViewById(R.id.calendar_btn_week);
		calendar_btn_week.setChecked(false);
		calendar_btn_week = (ToggleButton) findViewById(R.id.calendar_btn_week);

		calendar_btn_next = (ImageView) findViewById(R.id.calendar_btn_next);
		calendar_btn_prev = (ImageView) findViewById(R.id.calendar_btn_prev);
		calendar_tv_date = (TextView) findViewById(R.id.calendar_tv_date);

		// Construct the week calendar view
		calendar_switcher_week = (HorizontalPanelSwitcher) findViewById(R.id.calendar_switcher_week);

		mThisWeekParentView = new RelativeLayout(this);
		mThisWeekParentView.setLayoutParams(DAY_PARENT_LAYOUT_PARAMS);

		mPrevWeekParentView = new RelativeLayout(this);
		mPrevWeekParentView.setLayoutParams(DAY_PARENT_LAYOUT_PARAMS);

		mNextWeekParentView = new RelativeLayout(this);
		mNextWeekParentView.setLayoutParams(DAY_PARENT_LAYOUT_PARAMS);

		calendar_switcher_week.setCenterLayout(mThisWeekParentView);
		calendar_switcher_week.setLeftLayout(mPrevWeekParentView);
		calendar_switcher_week.setRightLayout(mNextWeekParentView);

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		// Initialize this week
		mThisWeekView = inflater.inflate(R.layout.layout_week, mThisWeekParentView);
		mThisWeekLayout = (LinearLayout) mThisWeekView.findViewById(R.id.calendar_week_courses);
		mThisWeekScroll = (ObservableScrollView) mThisWeekView.findViewById(R.id.calendar_sv_week);

		// Initialize next week
		mNextWeekView = inflater.inflate(R.layout.layout_week, mNextWeekParentView);
		mNextWeekLayout = (LinearLayout) mNextWeekView.findViewById(R.id.calendar_week_courses);
		mNextWeekScroll = (ObservableScrollView) mNextWeekView.findViewById(R.id.calendar_sv_week);

		// Initialize previous week
		mPrevWeekView = inflater.inflate(R.layout.layout_week, mPrevWeekParentView);
		mPrevWeekLayout = (LinearLayout) mPrevWeekView.findViewById(R.id.calendar_week_courses);
		mPrevWeekScroll = (ObservableScrollView) mPrevWeekView.findViewById(R.id.calendar_sv_week);
	}

	/**
	 * Initializes the listeners.
	 */
	private void initListeners() {
		// Switch between day and week views
		OnClickListener buttonListener = new OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.calendar_btn_day:
					showDayView();
					break;
				case R.id.calendar_btn_week:
					showWeekView();
					break;
				}
			}
		};
		calendar_btn_day.setOnClickListener(buttonListener);
		calendar_btn_week.setOnClickListener(buttonListener);

		// Move to next day when hitting the next button
		calendar_btn_next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mDayMode) {
					calendar_switcher_day.switchPanels(true);
					fadeInOut(calendar_tv_date, new StartFadeInListener() {
						public void onStartFadeIn() {
							mThisDay.add(Calendar.DAY_OF_MONTH, 1);
							mNextDay.add(Calendar.DAY_OF_MONTH, 1);
							mPrevDay.add(Calendar.DAY_OF_MONTH, 1);
							calendar_tv_date.setText(DAY_FORMAT.format(mThisDay.getTime()));
						}
					});
				} else {
					calendar_switcher_week.switchPanels(true);
					fadeInOut(calendar_tv_date, new StartFadeInListener() {
						public void onStartFadeIn() {
							mThisWeek.add(Calendar.WEEK_OF_MONTH, 1);
							mNextWeek.add(Calendar.WEEK_OF_MONTH, 1);
							mPrevWeek.add(Calendar.WEEK_OF_MONTH, 1);
							calendar_tv_date.setText("Week of " + WEEK_FORMAT.format(mThisWeek.getTime()));
						}
					});
				}
			}
		});

		// Move to previous day when hitting the previous button
		calendar_btn_prev.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mDayMode) {
					calendar_switcher_day.switchPanels(false);
					fadeInOut(calendar_tv_date, new StartFadeInListener() {
						public void onStartFadeIn() {
							mThisDay.add(Calendar.DAY_OF_MONTH, -1);
							mNextDay.add(Calendar.DAY_OF_MONTH, -1);
							mPrevDay.add(Calendar.DAY_OF_MONTH, -1);
							calendar_tv_date.setText(DAY_FORMAT.format(mThisDay.getTime()));
						}
					});
				} else {
					calendar_switcher_week.switchPanels(false);
					fadeInOut(calendar_tv_date, new StartFadeInListener() {
						public void onStartFadeIn() {
							mThisWeek.add(Calendar.WEEK_OF_MONTH, -1);
							mNextWeek.add(Calendar.WEEK_OF_MONTH, -1);
							mPrevWeek.add(Calendar.WEEK_OF_MONTH, -1);
							calendar_tv_date.setText("Week of " + WEEK_FORMAT.format(mThisWeek.getTime()));
						}
					});
				}
			}
		});

		initDayListeners();
		initWeekListeners();
	}

	/**
	 * Initializes listeners associated with widgets in the day view
	 */
	private void initDayListeners() {
		// React to switching the day in the panel switcher
		calendar_switcher_day.setOnPanelSwitchListener(new OnPanelSwitchListener() {
			public void switchRight() {
				initDayView();
			}

			public void switchLeft() {
				initDayView();
			}
		});

		// Lock the scroll view and disappear the buttons while changing days
		calendar_switcher_day.setOnDragListener(new OnDragListener() {
			public void dragStart() {
				setScrollLock(true);
			}

			public void dragEnd(boolean isSwitching, final int targetPanel) {
				setScrollLock(false);
				if (isSwitching) {
					if (targetPanel == HorizontalPanelSwitcher.PANEL_LEFT) {
						mThisDay.add(Calendar.DAY_OF_MONTH, -1);
						mNextDay.add(Calendar.DAY_OF_MONTH, -1);
						mPrevDay.add(Calendar.DAY_OF_MONTH, -1);
					}
					else if (targetPanel == HorizontalPanelSwitcher.PANEL_RIGHT) {
						mThisDay.add(Calendar.DAY_OF_MONTH, 1);
						mNextDay.add(Calendar.DAY_OF_MONTH, 1);
						mPrevDay.add(Calendar.DAY_OF_MONTH, 1);
					}
					fadeInOut(calendar_tv_date, new StartFadeInListener() {
						public void onStartFadeIn() {
							if (targetPanel == HorizontalPanelSwitcher.PANEL_LEFT) {
								calendar_tv_date.setText(DAY_FORMAT.format(mThisDay.getTime()));
							}
							else if (targetPanel == HorizontalPanelSwitcher.PANEL_RIGHT) {
								calendar_tv_date.setText(DAY_FORMAT.format(mThisDay.getTime()));
							}
						}
					});
				}
			}
		});

		// Intercept touch events while "mScrollLock" is true
		mThisDayScroll.setOnTouchListener(new ScrollView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return mScrollLock;
			}
		});

		// Synchronize the previous day's and the next day's scroll views with this day's scroll view
		mThisDayScroll.setOnScrollListener(new ScrollViewListener() {
			public void onScrollChanged(ObservableScrollView view, int x, int y, int oldx, int oldy) {
				mNextDayScroll.scrollTo(0, y);
				mPrevDayScroll.scrollTo(0, y);
			}
		});
	}

	/**
	 * Initializes listeners associated with widgets in the week view
	 */
	private void initWeekListeners() {
		// React to switching the week in the panel switcher
		calendar_switcher_week.setOnPanelSwitchListener(new OnPanelSwitchListener() {
			public void switchRight() {
				initWeekView();
			}

			public void switchLeft() {
				initWeekView();
			}
		});

		// Lock the scroll view and disappear the buttons while changing weeks
		calendar_switcher_week.setOnDragListener(new OnDragListener() {
			public void dragStart() {
				setScrollLock(true);
			}

			public void dragEnd(boolean isSwitching, final int targetPanel) {
				setScrollLock(false);
				if (isSwitching) {
					if (targetPanel == HorizontalPanelSwitcher.PANEL_LEFT) {
						mPrevWeek.add(Calendar.WEEK_OF_YEAR, -1);
						mThisWeek.add(Calendar.WEEK_OF_YEAR, -1);
						mNextWeek.add(Calendar.WEEK_OF_YEAR, -1);
					}
					else if (targetPanel == HorizontalPanelSwitcher.PANEL_RIGHT) {
						mPrevWeek.add(Calendar.WEEK_OF_YEAR, 1);
						mThisWeek.add(Calendar.WEEK_OF_YEAR, 1);
						mNextWeek.add(Calendar.WEEK_OF_YEAR, 1);
					}
					fadeInOut(calendar_tv_date, new StartFadeInListener() {
						public void onStartFadeIn() {
							if (targetPanel == HorizontalPanelSwitcher.PANEL_LEFT) {
								calendar_tv_date.setText("Week of " + WEEK_FORMAT.format(mThisWeek.getTime()));
							}
							else if (targetPanel == HorizontalPanelSwitcher.PANEL_RIGHT) {
								calendar_tv_date.setText("Week of " + WEEK_FORMAT.format(mThisWeek.getTime()));
							}
						}
					});
				}
			}
		});

		// Intercept touch events while "mScrollLock" is true
		mThisWeekScroll.setOnTouchListener(new ScrollView.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return mScrollLock;
			}
		});

		// Synchronize the previous week's and the next week's scroll views with this week's scroll view
		mThisWeekScroll.setOnScrollListener(new ScrollViewListener() {
			public void onScrollChanged(ObservableScrollView view, int x, int y, int oldx, int oldy) {
				mNextWeekScroll.scrollTo(0, y);
				mPrevWeekScroll.scrollTo(0, y);
			}
		});
	}

	private void initDayView() {
		// Remove any existing day views
		mThisDayCourses.removeAllViews();
		mNextDayCourses.removeAllViews();
		mPrevDayCourses.removeAllViews();

		// Scroll prev/next days to the current day scroll
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

		// Add hour and half-hour markers to the view
		addTimeLinesToView(mThisDayCourses);
		addTimeLinesToView(mNextDayCourses);
		addTimeLinesToView(mPrevDayCourses);

		// Add courses to the current day
		if (mCurrentTerm.getStartDate().before(mThisDay) && mCurrentTerm.getEndDate().after(mThisDay)) {
			for (Course course : mCourses) {
				List<TimePlaceBlock> blocks = course.getBlocksOnDay(mThisDay.get(Calendar.DAY_OF_WEEK) - 1);
				for (TimePlaceBlock block : blocks) {
					addCourseToDay(course, block, mThisDay, mThisDayCourseBlocks, mThisDayCourses);
				}
			}

			// Add exams to the current day
			for (Exam exam : mExams) {
				Calendar start = exam.getBlock().getStartTime();
				if (start.get(Calendar.YEAR) == mThisDay.get(Calendar.YEAR) &&
						start.get(Calendar.DAY_OF_YEAR) == mThisDay.get(Calendar.DAY_OF_YEAR)) {
					addExamToDay(exam, mThisDay, mThisDayCourseBlocks, mThisDayCourses);
				}
			}
		}

		// Add courses for next day
		if (mCurrentTerm.getStartDate().before(mNextDay) && mCurrentTerm.getEndDate().after(mNextDay)) {
			for (Course course : mCourses) {
				List<TimePlaceBlock> blocks = course.getBlocksOnDay(mNextDay.get(Calendar.DAY_OF_WEEK) - 1);
				for (TimePlaceBlock block : blocks) {
					addCourseToDay(course, block, mNextDay, mNextDayCourseBlocks, mNextDayCourses);
				}
			}

			// Add exams to the next day
			for (Exam exam : mExams) {
				Calendar start = exam.getBlock().getStartTime();
				if (start.get(Calendar.YEAR) == mNextDay.get(Calendar.YEAR) &&
						start.get(Calendar.DAY_OF_YEAR) == mNextDay.get(Calendar.DAY_OF_YEAR)) {
					addExamToDay(exam, mNextDay, mNextDayCourseBlocks, mNextDayCourses);
				}
			}
		}

		// Add courses for previous day
		if (mCurrentTerm.getStartDate().before(mPrevDay) && mCurrentTerm.getEndDate().after(mPrevDay)) {
			for (Course course : mCourses) {
				List<TimePlaceBlock> blocks = course.getBlocksOnDay(mPrevDay.get(Calendar.DAY_OF_WEEK) - 1);
				for (TimePlaceBlock block : blocks) {
					addCourseToDay(course, block, mPrevDay, mPrevDayCourseBlocks, mPrevDayCourses);
				}
			}

			// Add exams to the previous day
			for (Exam exam : mExams) {
				Calendar start = exam.getBlock().getStartTime();
				if (start.get(Calendar.YEAR) == mPrevDay.get(Calendar.YEAR) &&
						start.get(Calendar.DAY_OF_YEAR) == mPrevDay.get(Calendar.DAY_OF_YEAR)) {
					addExamToDay(exam, mPrevDay, mPrevDayCourseBlocks, mPrevDayCourses);
				}
			}
		}

		// If viewing today show a red line for current time
		Calendar currentDay = Calendar.getInstance();
		if (mThisDay.get(Calendar.YEAR) == currentDay.get(Calendar.YEAR) && mThisDay.get(Calendar.DAY_OF_YEAR) == currentDay.get(Calendar.DAY_OF_YEAR)) {
			addCurrentTimeLine();
		}
	}

	private void addCurrentTimeLine() {
		mCurrentTimeLine = new View(this);
		mCurrentTimeLine.setBackgroundResource(R.drawable.calendar_line);
		mCurrentTimeLineParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 4);
		mCurrentTimeLineParams.setMargins(0, (int) ((Utility.minFromMidnight(Calendar.getInstance()) + DAY_VIEW_BUFFER - 1) * mDensity + 0.5f), 0, 0);
		mCurrentTimeLine.setLayoutParams(mCurrentTimeLineParams);
		mThisDayCourses.addView(mCurrentTimeLine);

		TimerTask updateLine = new TimerTask() {
			@Override
			public void run() {
				mCurrentTimeLine.post(new Runnable() {
					public void run() {
						mCurrentTimeLineParams.setMargins(0, (int) ((Utility.minFromMidnight(Calendar.getInstance()) + DAY_VIEW_BUFFER - 1) * mDensity + 0.5f), 0, 0);
						mCurrentTimeLine.setLayoutParams(mCurrentTimeLineParams);
					}
				});
			}
		};
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(updateLine, 1000 - Calendar.getInstance().get(Calendar.MILLISECOND), 1000);
	}

	private void addTimeLinesToView(RelativeLayout iView) {
		for (int i = 0; i < 2 * Utility.HOURS_PER_DAY; ++i) {
			// Sets the block distance from the top of the layout
			View line = new View(this);
			line.setBackgroundResource((i % 2 == 0) ? R.drawable.hour_line : R.drawable.halfhour_line);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 2);
			params.setMargins(0, (int) ((30 * i + DAY_VIEW_BUFFER - 1) * mDensity + 0.5f), 0, 0);
			line.setLayoutParams(params);
			iView.addView(line);
		}
	}

	private void initWeekView() {
		// Remove any existing week views
		mThisWeekLayout.removeAllViews();
		mNextWeekLayout.removeAllViews();
		mPrevWeekLayout.removeAllViews();

		// Initialize the current week
		initSingleWeek(mThisWeek, mThisWeekCourseBlocks, mThisWeekLayout);

		// Initialize the next week
		initSingleWeek(mNextWeek, mNextWeekCourseBlocks, mNextWeekLayout);

		// Initialize the previous week
		initSingleWeek(mPrevWeek, mPrevWeekCourseBlocks, mPrevWeekLayout);
	}

	private void initSingleWeek(Calendar iFirstDay, LinkedList<TextView> iBlocks, LinearLayout iWeekLayout) {
		Calendar day = (Calendar) iFirstDay.clone();

		// Tree map to store sorted blocks and the associated courses
		TreeMap<TimePlaceBlock, ContentValueItem> courseAndExamBlocks = new TreeMap<TimePlaceBlock, ContentValueItem>(TIME_COMPARATOR);
		// Add blocks for each day
		for (int i = 0; i < DAYS_IN_WEEK; ++i) {
			// Initialize the day header block
			addWeekDayHeaderBlock(day, iBlocks, iWeekLayout);

			// If the day is in the current term
			if (mCurrentTerm.getStartDate().before(day) && mCurrentTerm.getEndDate().after(day)) {
				// Add time blocks to tree map for sorting by time of day
				for (Course course : mCourses) {
					List<TimePlaceBlock> blocks = course.getBlocksOnDay(i);
					for (TimePlaceBlock block : blocks) {
						courseAndExamBlocks.put(block, course);
					}
				}
				
				// Add exams for the current day
				for (Exam exam : mExams) {
					Calendar start = exam.getBlock().getStartTime();
					if (start.get(Calendar.YEAR) == day.get(Calendar.YEAR) &&
							start.get(Calendar.DAY_OF_YEAR) == day.get(Calendar.DAY_OF_YEAR)) {
						courseAndExamBlocks.put(exam.getBlock(), exam);
					}
				}

				// Add time blocks to the view in chronological order
				for (Entry<TimePlaceBlock, ContentValueItem> entry : courseAndExamBlocks.entrySet()) {
					if (entry.getValue() instanceof Course) {
						addCourseToWeek((Course)entry.getValue(), entry.getKey(), day, iBlocks, iWeekLayout);
					}
					else if (entry.getValue() instanceof Exam) {
						addExamToWeek((Exam)entry.getValue(), day, iBlocks, iWeekLayout);
					}
				}

				courseAndExamBlocks.clear();
			}

			// Increment the day by 1
			day.add(Calendar.DAY_OF_WEEK, 1);
		}
	}

	private void addWeekDayHeaderBlock(Calendar day, LinkedList<TextView> iBlockList, LinearLayout iLayout) {
		TextView weekDayBlock = new TextView(this);
		weekDayBlock.setTextColor(Color.BLACK);
		weekDayBlock.setBackgroundColor(Color.LTGRAY);
		weekDayBlock.setText(DAY_FORMAT.format(day.getTime()));
		weekDayBlock.setBackgroundResource(R.drawable.calendar_block);
		weekDayBlock.setLayoutParams(WEEK_LAYOUT_PARAMS);
		weekDayBlock.setTag(day.clone());

		weekDayBlock.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Calendar day = (Calendar) view.getTag();
				mThisDay = (Calendar) day.clone();
				mNextDay = (Calendar) mThisDay.clone();
				mNextDay.add(Calendar.DAY_OF_YEAR, 1);
				mPrevDay = (Calendar) mThisDay.clone();
				mPrevDay.add(Calendar.DAY_OF_YEAR, -1);
				showDayView();
			}
		});
		iBlockList.add(weekDayBlock);
		iLayout.addView(weekDayBlock);
	}

	private void addCourseToDay(Course iCourse, TimePlaceBlock iBlock, Calendar iDay, LinkedList<TextView> iBlockList, RelativeLayout iLayout) {

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

	private void addExamToDay(Exam iExam, Calendar iDay, LinkedList<TextView> iDayBlocks, RelativeLayout iDayLayout) {
		// Initialize the block's colors, text, and listeners
		TextView examBlock = getExamBlock(iExam);

		// Sets the block height appropriately (1 minute = 1 dp)
		int startOffset = Utility.minFromMidnight(iExam.getBlock().getStartTime());
		int blockSize = Utility.minFromMidnight(iExam.getBlock().getEndTime()) - startOffset;
		int blockHeight_dp = (int) (blockSize * mDensity + 0.5f);

		// Sets the block distance from the top of the layout
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, blockHeight_dp);
		params.setMargins(0, (int) ((startOffset + DAY_VIEW_BUFFER) * mDensity + 0.5f), 0, 0);
		examBlock.setLayoutParams(params);
		examBlock.setTag(iDay.clone());

		// Add the block to the list of blocks and the layout
		iDayBlocks.add(examBlock);
		iDayLayout.addView(examBlock);
	}

	private void addCourseToWeek(Course iCourse, TimePlaceBlock iBlock, Calendar iDay, LinkedList<TextView> iCourseBlocks, LinearLayout iWeekLayout) {
		// Initialize the block's colors, text, and listeners
		TextView weekDayBlock = getCourseBlock(iCourse, iBlock);
		weekDayBlock.setTag(iDay.clone());

		weekDayBlock.setLayoutParams(WEEK_LAYOUT_PARAMS);

		// Add the week's day block to the list and the linear layout
		iCourseBlocks.add(weekDayBlock);
		iWeekLayout.addView(weekDayBlock);
	}
	
	private void addExamToWeek(Exam iCourse, Calendar iDay, LinkedList<TextView> iCourseBlocks, LinearLayout iWeekLayout) {
		// Initialize the block's colors, text, and listeners
		TextView weekDayBlock = getExamBlock(iCourse);
		weekDayBlock.setTag(iDay.clone());

		weekDayBlock.setLayoutParams(WEEK_LAYOUT_PARAMS);

		// Add the week's day block to the list and the linear layout
		iCourseBlocks.add(weekDayBlock);
		iWeekLayout.addView(weekDayBlock);
	}

	/**
	 * Gets a widget representing a course's block on the calendar
	 * @param iCourse The course for this course block
	 * @param iBlock The data for this block
	 * @return A text view widget for this course ready to be placed in a day or week view
	 */
	private TextView getCourseBlock(Course iCourse, TimePlaceBlock iBlock) {
		TextView courseBlock = new TextView(this);
		courseBlock.setTextColor(Color.BLACK);
		courseBlock.setBackgroundColor(Color.GREEN);
		courseBlock.setText(iCourse.toString() + "\n" + iBlock.toTimeString() + "\n" + iBlock.getLocation());
		courseBlock.setBackgroundResource(R.drawable.class_block_green);
		courseBlock.setClickable(true);
		courseBlock.setOnClickListener(new CourseClickListener(iCourse.getID(), iBlock.getID()));
		return courseBlock;
	}

	private TextView getExamBlock(final Exam iExam) {
		TextView examBlock = new TextView(this);
		examBlock.setTextColor(Color.YELLOW);
		examBlock.setBackgroundResource(R.drawable.exam_block);
		examBlock.setText(iExam.toString());
		examBlock.setClickable(true);
		examBlock.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent showExams = new Intent(CalendarActivity.this, CourseExamsActivity.class);
				showExams.putExtra("courseID", iExam.getCourse().getID());
				startActivity(showExams);
			}
		});
		return examBlock;
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

	/**
	 * Sets the scroll lock, which prevents the scroll views from scrolling
	 * @param iScrollLock
	 */
	public void setScrollLock(boolean iScrollLock) {
		mScrollLock = iScrollLock;
	}

	/**
	 * @return True if scrolling is locked
	 */
	public boolean getScrollLock() {
		return mScrollLock;
	}

	private void fadeInOut(final View iView, final StartFadeInListener iFadeListener) {
		iView.setEnabled(false);
		iView.postDelayed(new Runnable() {
			public void run() {
				iFadeListener.onStartFadeIn();
				iView.setVisibility(View.INVISIBLE);
				iView.setEnabled(true);
				iView.startAnimation(FADE_IN_ANIMATION);
				iView.setVisibility(View.VISIBLE);
			}
		}, ANIMATION_TIME);
		iView.startAnimation(FADE_OUT_ANIMATION);
	}

	private void showDayView() {
		mDayMode = true;
		calendar_btn_day.setChecked(true);
		calendar_btn_day.setBackgroundResource(R.drawable.button_depressed);
		calendar_btn_week.setChecked(false);
		calendar_btn_week.setBackgroundResource(R.drawable.button_default);
		calendar_switcher_day.setVisibility(View.VISIBLE);
		calendar_switcher_week.setVisibility(View.GONE);
		calendar_tv_date.setText(DAY_FORMAT.format(mThisDay.getTime()));
		initDayView();
	}

	private void showWeekView() {
		mDayMode = false;
		calendar_btn_day.setChecked(false);
		calendar_btn_day.setBackgroundResource(R.drawable.button_default);
		calendar_btn_week.setChecked(true);
		calendar_btn_week.setBackgroundResource(R.drawable.button_depressed);
		calendar_switcher_day.setVisibility(View.GONE);
		calendar_switcher_week.setVisibility(View.VISIBLE);
		calendar_tv_date.setText("Week of " + WEEK_FORMAT.format(mThisWeek.getTime()));
		initWeekView();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		
		// Term(s) may have been deleted and then this activity backed into
		mCurrentTerm = ((ScheduApplication) getApplication()).getCurrentTerm();

		if (mCurrentTerm != null) {
			mDayMode = true;
			mCourses.clear();
			mCourses.addAll(mCourseManager.getAllForTerm(mCurrentTerm.getID()));
			initDayView();
		} else {
			finish();
		}
	}
}
