package com.madinnovations.rmu.data.entities.common.creature;

import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.data.dao.combat.impl.AttackDaoDbImpl;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 *         Created 4/26/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class CreatureVarietyTest {
	@Before
	public void setup() {
		PowerMockito.mockStatic(Log.class);
	}

	@DataProvider
	public Object[][] createParseAttackSequenceData() {
		CreatureVariety creatureVariety = new CreatureVariety();
		creatureVariety.setAttackSequence("57B(6)gr>>;57M(5)be;45B(6)we;57B(6)all2WF");
		return new Object[][] {
				{"57B(6)gr"},
				{"57B(6)all2WF"},
				{"57B(6)gr>;57M(5)be;45B(6)we;57B(6)all2WF"},
				{"57B(6)gr>>;57M(5)be;45B(6)we;57B(6)all2WF"}
		};
	}

	@Test(dataProvider = "createParseAttackSequenceData")
	public void testParseAttackSequence(String attackSequence) {
		CreatureVariety creatureVariety = new CreatureVariety();
		creatureVariety.setAttackSequence(attackSequence);
//		creatureVariety.parseAttackSequence();
		assertEquals(1, 1);
	}
}
