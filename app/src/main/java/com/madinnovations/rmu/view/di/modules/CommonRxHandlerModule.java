package com.madinnovations.rmu.view.di.modules;

import com.madinnovations.rmu.controller.rxhandler.common.StatRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentCategoryRxHandler;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.TalentCategoryDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides instances of classes that create AndroidRx Observables for dependency injection.
 */
@Module(includes = ApplicationModule.class)
public class CommonRxHandlerModule {
	@Provides @Singleton
	public TalentCategoryRxHandler provideTalentCategoryRxHandler(TalentCategoryDao dao) {
		return new TalentCategoryRxHandler(dao);
	}
	@Provides @Singleton
	public StatRxHandler provideStatRxHandler(StatDao dao) {
		return new StatRxHandler(dao);
	}
}
