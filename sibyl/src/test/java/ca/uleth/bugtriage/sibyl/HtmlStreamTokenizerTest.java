package ca.uleth.bugtriage.sibyl;

import java.io.Reader;
import java.io.StringReader;

import junit.framework.TestCase;

import org.eclipse.mylar.internal.bugzilla.core.HtmlTag;

import ca.uleth.bugtriage.sibyl.mylar.HtmlStreamTokenizer;

public class HtmlStreamTokenizerTest extends TestCase {

	public void testUnescapedAttributes() throws Exception {
		String testString = "<input name=\"short_desc\" accesskey=\"s\" value=\"This &quot;contains&quot; a quote\" size=\"60\">";
		String unescapedText = "This &quot;contains&quot; a quote";
		Reader reader = new StringReader(testString);
		HtmlStreamTokenizer tokenizer = new HtmlStreamTokenizer(reader, null);
		tokenizer.escapeTagAttributes(false);
		HtmlTag tag = (HtmlTag)(tokenizer.nextToken().getValue());
		assertEquals(tag.getAttribute("value"), unescapedText);
	}
	
	public void testEscapedAttributes() throws Exception {
		String testString = "<input name=\"short_desc\" accesskey=\"s\" value=\"This &quot;contains&quot; a quote\" size=\"60\">";
		String escapedText = "This \"contains\" a quote";
		Reader reader = new StringReader(testString);
		HtmlStreamTokenizer tokenizer = new HtmlStreamTokenizer(reader, null);
		HtmlTag tag = (HtmlTag)(tokenizer.nextToken().getValue());
		assertEquals(tag.getAttribute("value"), escapedText);
	}
}
