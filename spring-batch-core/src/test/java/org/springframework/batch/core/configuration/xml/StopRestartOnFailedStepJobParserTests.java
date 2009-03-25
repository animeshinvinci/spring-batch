/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.batch.core.configuration.xml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Dave Syer
 * 
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class StopRestartOnFailedStepJobParserTests extends AbstractJobParserTests {

	@Test
	public void testStopRestartOnCompletedStep() throws Exception {

		//
		// First Launch
		//
		launchAndAssert("[s0, fail]");

		//
		// Second Launch
		//
		stepNamesList.clear();
		launchAndAssert("[fail]");

	}

	private void launchAndAssert(String stepNames) throws JobInstanceAlreadyCompleteException, JobRestartException,
			JobExecutionAlreadyRunningException {
		JobExecution jobExecution = createJobExecution();
		job.execute(jobExecution);
		assertEquals(stepNames, stepNamesList.toString());

		assertEquals(BatchStatus.STOPPED, jobExecution.getStatus());
		assertEquals(ExitStatus.STOPPED.getExitCode(), jobExecution.getExitStatus().getExitCode());

		StepExecution stepExecution1 = getStepExecution(jobExecution, "fail");
		assertEquals(BatchStatus.FAILED, stepExecution1.getStatus());
		assertEquals(ExitStatus.FAILED.getExitCode(), stepExecution1.getExitStatus().getExitCode());
	}

}
