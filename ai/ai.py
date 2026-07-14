from browser_use import Agent, Browser, ChatGoogle
import asyncio
from dotenv import load_dotenv
import logging
import datetime
import json
from prompts import scenarios

logging.basicConfig(level=logging.INFO)

TIMESTAMP = datetime.datetime.now().strftime("%Y%m%d_%H%M%S")
load_dotenv()

llm_gemini = ChatGoogle(model='gemini-3.1-flash-lite')

async def run_scenario(llm, model_name, scenario):
    browser = Browser()
    logging.info(f"Running: {model_name} | {scenario.scenario_name}")
    agent = Agent(
        task=scenario.steps,
        llm=llm,
        browser=browser,
        generate_gif=True,
        use_vision=True,
        max_actions_per_step=1,
        save_conversation_path=f"e2elogs/{model_name}/{scenario.scenario_name}/{TIMESTAMP}"
    )
    history = await agent.run()
    result = history.final_result()
    print(f"\nResult [{scenario.scenario_name}] is {result}")
    await browser.close()
    return result


async def main():
    results = {}
    for scenario in scenarios:
        try:
            results[scenario.scenario_name] = await run_scenario(
                llm_gemini, "gemini-3.1-flash-lite", scenario
            )
        except Exception as e:
            logging.error(f"Scenario {scenario.scenario_name} crashed: {e}")
            results[scenario.scenario_name] = f"ERROR: {e}"

        with open("results.json", "w", encoding="utf-8") as f:
            json.dump(results, f, ensure_ascii=False, indent=2)

    return results


if __name__ == '__main__':
    asyncio.run(main())