package lib.experiments;

import static java.util.Calendar.*;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

public class FormatDate implements Comparable<FormatDate>, Serializable {
	private static final long serialVersionUID = -18939474933217770L;

	public static String readbleTime(long ms) {
		StringBuffer sb = new StringBuffer();
		long s = ms / 1000;
		if (s > DAY_S) {
			sb.append(s / DAY_S +" day ");
			s %= DAY_S;
		}
		if (s > HOUR_S) {
			sb.append(s / HOUR_S +" h ");
			s %= HOUR_S;
		}
		if (s > MIN_S) {
			sb.append(s / MIN_S +" min ");
		}
		sb.append(ms % MIN_MS / 1000d +" s");
		return sb.toString();
	}

	/**
	 * Returns date in year/month/day hour:min:sec format.
	 * @return String representing current date and time
	 */
	public static FormatDate getDate() {
		return new FormatDate(Calendar.getInstance());
	}



	private static long MIN_S = 60;
	private static long MIN_MS = MIN_S * 1000;
	private static long HOUR_S = 3600;
	private static long DAY_S = 24 * HOUR_S;


	public final Calendar cal;
	public final long timeZone;

	public FormatDate(Calendar c) {
		cal = c;
		this.timeZone = TimeZone.getDefault().getOffset(cal.getTimeInMillis()) / 1000;	}

	@Override
	public String toString() {
		return String.format("%d-%02d-%02d %02d:%02d:%02d%+02d:%02d",
				cal.get(YEAR), cal.get(MONTH) + 1, cal.get(DATE),
				cal.get(HOUR_OF_DAY), cal.get(MINUTE), cal.get(SECOND),
				timeZone /  3600, timeZone % 3600);
	}

	@Override
	public int compareTo(FormatDate o) {
		if (o == null) throw new NullPointerException();
		return cal.compareTo(o.cal);
	}
}
