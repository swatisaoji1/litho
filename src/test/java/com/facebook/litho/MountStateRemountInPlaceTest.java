/**
 * Copyright (c) 2014-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.litho;

import com.facebook.yoga.YogaAlign;

import com.facebook.yoga.YogaFlexDirection;

import android.graphics.Color;

import com.facebook.litho.testing.ComponentTestHelper;
import com.facebook.litho.testing.testrunner.ComponentsTestRunner;
import com.facebook.litho.widget.SolidColor;
import com.facebook.litho.widget.Text;
import com.facebook.litho.testing.TestComponent;
import com.facebook.litho.testing.TestDrawableComponent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import static com.facebook.litho.ComponentsLogger.EVENT_PREPARE_MOUNT;
import static com.facebook.litho.ComponentsLogger.PARAM_MOVED_COUNT;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(ComponentsTestRunner.class)
public class MountStateRemountInPlaceTest {
  private ComponentContext mContext;
  private ComponentsLogger mComponentsLogger;

  @Before
  public void setup() {
    mComponentsLogger = mock(ComponentsLogger.class);
    mContext = new ComponentContext(RuntimeEnvironment.application, "tag", mComponentsLogger);
  }

  @Test
  public void testMountUnmountWithShouldUpdate() {
    final TestComponent firstComponent =
        TestDrawableComponent.create(mContext)
            .unique()
            .build();

    final ComponentView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(firstComponent)
                .build();
          }
        });

    assertTrue(firstComponent.wasOnMountCalled());
    assertTrue(firstComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());

    final TestComponent secondComponent =
        TestDrawableComponent.create(mContext)
            .unique()
            .build();

    componentView.getComponent().setRoot(new InlineLayoutSpec() {
      @Override
      protected ComponentLayout onCreateLayout(ComponentContext c) {
        return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
            .child(secondComponent)
            .build();
      }
    });

    assertTrue(secondComponent.wasOnMountCalled());
    assertTrue(secondComponent.wasOnBindCalled());
    assertTrue(firstComponent.wasOnUnmountCalled());
  }

  @Test
  public void testMountUnmountWithNoShouldUpdate() {
    final TestComponent firstComponent =
        TestDrawableComponent.create(mContext)
            .build();

    final ComponentView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(firstComponent)
                .build();
          }
        });

    assertTrue(firstComponent.wasOnMountCalled());
    assertTrue(firstComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());

    final TestComponent secondComponent =
        TestDrawableComponent.create(mContext)
            .build();

    componentView.getComponent().setRoot(new InlineLayoutSpec() {
      @Override
      protected ComponentLayout onCreateLayout(ComponentContext c) {
        return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
            .child(secondComponent)
            .build();
      }
    });

    assertFalse(secondComponent.wasOnMountCalled());
    assertTrue(secondComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());
  }

  @Test
  public void testMountUnmountWithNoShouldUpdateAndDifferentSize() {
    final TestComponent firstComponent =
        TestDrawableComponent
            .create(
                mContext,
                0,
                0,
                true,
                true,
                true,
                false,
                false,
                true /*isMountSizeDependent*/)
            .measuredHeight(10)
            .build();

    final ComponentView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(firstComponent)
                .build();
          }
        });

    assertTrue(firstComponent.wasOnMountCalled());
    assertTrue(firstComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());

    final TestComponent secondComponent =
        TestDrawableComponent
            .create(
                mContext,
                0,
                0,
                true,
                true,
                true,
                false,
                false,
                true /*isMountSizeDependent*/)
            .measuredHeight(11)
            .build();

    componentView.getComponent().setRoot(new InlineLayoutSpec() {
      @Override
      protected ComponentLayout onCreateLayout(ComponentContext c) {
        return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
            .child(secondComponent)
            .build();
      }
    });

    assertTrue(secondComponent.wasOnMountCalled());
    assertTrue(secondComponent.wasOnBindCalled());
    assertTrue(firstComponent.wasOnUnmountCalled());
  }

  @Test
  public void testMountUnmountWithNoShouldUpdateAndSameSize() {
    final TestComponent firstComponent =
        TestDrawableComponent
            .create(
                mContext,
                0,
                0,
                true,
                true,
                true,
                false,
                false,
                true /*isMountSizeDependent*/)
            .measuredHeight(10)
            .build();

    final ComponentView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(firstComponent)
                .build();
          }
        });

    assertTrue(firstComponent.wasOnMountCalled());
    assertTrue(firstComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());

    final TestComponent secondComponent =
        TestDrawableComponent
            .create(
                mContext,
                0,
                0,
                true,
                true,
                true,
                false,
                false,
                true /*isMountSizeDependent*/)
            .measuredHeight(10)
            .build();

    componentView.getComponent().setRoot(new InlineLayoutSpec() {
      @Override
      protected ComponentLayout onCreateLayout(ComponentContext c) {
        return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
            .child(secondComponent)
            .build();
      }
    });

    assertFalse(secondComponent.wasOnMountCalled());
    assertTrue(secondComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());
  }

  @Test
  public void testMountUnmountWithNoShouldUpdateAndDifferentMeasures() {
    final TestComponent firstComponent =
        TestDrawableComponent.create(mContext)
            .build();

    final ComponentView componentView = ComponentTestHelper.mountComponent(
        new ComponentView(mContext),
        ComponentTree.create(mContext, new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(firstComponent)
                .build();
          }
        })
            .incrementalMount(false)
            .build(),
        makeMeasureSpec(100, AT_MOST),
        makeMeasureSpec(100, AT_MOST));

    assertTrue(firstComponent.wasOnMountCalled());
    assertTrue(firstComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());

    final TestComponent secondComponent =
        TestDrawableComponent.create(mContext)
            .build();

    componentView.getComponent().setRoot(new InlineLayoutSpec() {
      @Override
      protected ComponentLayout onCreateLayout(ComponentContext c) {
        return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
            .child(secondComponent)
            .widthPx(10)
            .heightPx(10)
            .build();
      }
    });

    assertTrue(componentView.isLayoutRequested());
    assertFalse(secondComponent.wasOnMountCalled());
    assertFalse(secondComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());
  }

  @Test
  public void testMountUnmountWithNoShouldUpdateAndSameMeasures() {
    final TestComponent firstComponent =
        TestDrawableComponent.create(mContext, 0, 0, true, true, true, false, false, true)
            .build();

    final ComponentView componentView = ComponentTestHelper.mountComponent(
        new ComponentView(mContext),
        ComponentTree.create(mContext, new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(firstComponent)
                .build();
          }
        })
            .incrementalMount(false)
            .build(),
        makeMeasureSpec(100, EXACTLY),
        makeMeasureSpec(100, EXACTLY));

    assertTrue(firstComponent.wasOnMountCalled());
    assertTrue(firstComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());

    final TestComponent secondComponent =
        TestDrawableComponent.create(mContext)
            .build();

    componentView.getComponent().setRoot(new InlineLayoutSpec() {
      @Override
      protected ComponentLayout onCreateLayout(ComponentContext c) {
        return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
            .child(secondComponent)
            .widthPx(10)
            .heightPx(10)
            .build();
      }
    });

    assertFalse(componentView.isLayoutRequested());
    assertTrue(secondComponent.wasOnMountCalled());
    assertTrue(secondComponent.wasOnBindCalled());
    assertTrue(firstComponent.wasOnUnmountCalled());
  }

  @Test
  public void testRebindWithNoShouldUpdateAndSameMeasures() {
    final TestComponent firstComponent =
        TestDrawableComponent.create(mContext)
            .build();

    final ComponentView componentView = ComponentTestHelper.mountComponent(
        new ComponentView(mContext),
        ComponentTree.create(mContext, new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(firstComponent)
                .build();
          }
        })
            .incrementalMount(false)
            .build(),
        makeMeasureSpec(100, EXACTLY),
        makeMeasureSpec(100, EXACTLY));

    assertTrue(firstComponent.wasOnMountCalled());
    assertTrue(firstComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());

    final TestComponent secondComponent =
        TestDrawableComponent.create(mContext)
            .build();

    componentView.getComponent().setRoot(new InlineLayoutSpec() {
      @Override
      protected ComponentLayout onCreateLayout(ComponentContext c) {
        return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
            .child(secondComponent)
            .widthPx(10)
            .heightPx(10)
            .build();
      }
    });

    assertFalse(componentView.isLayoutRequested());
    assertFalse(secondComponent.wasOnMountCalled());
    assertTrue(secondComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());
  }

  @Test
  public void testMountUnmountWithSkipShouldUpdate() {
    final TestComponent firstComponent =
        TestDrawableComponent.create(mContext)
            .color(Color.BLACK)
            .build();

    final ComponentView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(firstComponent)
                .build();
          }
        });

    assertTrue(firstComponent.wasOnMountCalled());
    assertTrue(firstComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());

    final TestComponent secondComponent =
        TestDrawableComponent.create(mContext)
            .color(Color.BLACK)
            .build();

    componentView.getComponent().setRoot(new InlineLayoutSpec() {
      @Override
      protected ComponentLayout onCreateLayout(ComponentContext c) {
        return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
            .child(secondComponent)
            .build();
      }
    });

    assertFalse(secondComponent.wasOnMountCalled());
    assertTrue(secondComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());
  }

  @Test
  public void testMountUnmountWithSkipShouldUpdateAndRemount() {
    final TestComponent firstComponent =
        TestDrawableComponent.create(mContext)
            .color(Color.BLACK)
            .build();

    final ComponentView componentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(firstComponent)
                .build();
          }
        });

    assertTrue(firstComponent.wasOnMountCalled());
    assertTrue(firstComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());

    final TestComponent secondComponent =
        TestDrawableComponent.create(mContext)
            .color(Color.WHITE)
            .build();

    componentView.getComponent().setRoot(new InlineLayoutSpec() {
      @Override
      protected ComponentLayout onCreateLayout(ComponentContext c) {
        return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
            .child(secondComponent)
            .build();
      }
    });

    assertTrue(secondComponent.wasOnMountCalled());
    assertTrue(secondComponent.wasOnBindCalled());
    assertTrue(firstComponent.wasOnUnmountCalled());
  }

  @Test
  public void testMountUnmountDoesNotSkipShouldUpdateAndRemount() {
    final TestComponent firstComponent =
        TestDrawableComponent.create(mContext)
            .unique()
            .build();

    final ComponentView firstComponentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(firstComponent)
                .build();
          }
        });

    assertTrue(firstComponent.wasOnMountCalled());
    assertTrue(firstComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());

    final TestComponent secondComponent =
        TestDrawableComponent.create(mContext)
            .unique()
            .build();

    final ComponentTree secondTree = ComponentTree.create(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(secondComponent)
                .build();
          }
        })
        .incrementalMount(false)
        .build();
    secondTree.setSizeSpec(100, 100);

    final TestComponent thirdComponent =
        TestDrawableComponent.create(mContext)
            .build();

    secondTree.setRoot(new InlineLayoutSpec() {
      @Override
      protected ComponentLayout onCreateLayout(ComponentContext c) {
        return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
            .child(thirdComponent)
            .build();
      }
    });

    ComponentTestHelper.mountComponent(firstComponentView, secondTree);

    assertTrue(thirdComponent.wasOnMountCalled());
    assertTrue(thirdComponent.wasOnBindCalled());
    assertTrue(firstComponent.wasOnUnmountCalled());
  }

  @Test
  public void testSkipShouldUpdateAndRemountForUnsupportedComponent() {
    final TestComponent firstComponent =
        TestDrawableComponent.create(
            mContext,
            false,
            true,
            true,
            false,
            false)
            .build();

    final ComponentView firstComponentView = ComponentTestHelper.mountComponent(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(firstComponent)
                .build();
          }
        });

    assertTrue(firstComponent.wasOnMountCalled());
    assertTrue(firstComponent.wasOnBindCalled());
    assertFalse(firstComponent.wasOnUnmountCalled());

    final TestComponent secondComponent =
        TestDrawableComponent.create(
            mContext,
            false,
            true,
            true,
            false,
            false)
            .build();

    final ComponentTree secondTree = ComponentTree.create(
        mContext,
        new InlineLayoutSpec() {
          @Override
          protected ComponentLayout onCreateLayout(ComponentContext c) {
            return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                .child(secondComponent)
                .build();
          }
        })
        .incrementalMount(false)
        .build();
    secondTree.setSizeSpec(100, 100);

    ComponentTestHelper.mountComponent(firstComponentView, secondTree);

    assertTrue(secondComponent.wasOnMountCalled());
    assertTrue(secondComponent.wasOnBindCalled());
    assertTrue(firstComponent.wasOnUnmountCalled());
  }

  @Test
  public void testRemountSameSubTreeWithDifferentParentHost() {
    final TestComponent firstComponent =
        TestDrawableComponent.create(
            mContext,
            false,
            true,
            true,
            false,
            false)
            .build();

    InlineLayoutSpec firstLayout = new InlineLayoutSpec() {
      @Override
      protected ComponentLayout onCreateLayout(ComponentContext c) {
        return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
            .child(
                Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                    .clickHandler(c.newEventHandler(3))
                    .child(
                        Text.create(c).text("test")))
            .child(
                Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                    .clickHandler(c.newEventHandler(2))
                    .child(
                        Text.create(c).text("test2"))
                    .child(
                        Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                            .clickHandler(c.newEventHandler(1))
                            .child(
                                firstComponent)
                            .child(
                                SolidColor.create(c).color(Color.GREEN))))
            .build();
      }
    };

    final InlineLayoutSpec secondLayout = new InlineLayoutSpec() {
      @Override
      protected ComponentLayout onCreateLayout(ComponentContext c) {
        return Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
            .child(
                Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                    .clickHandler(c.newEventHandler(3))
                    .child(
                        Text.create(c).text("test"))
                    .child(
                        Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                            .clickHandler(c.newEventHandler(1))
                            .child(
                                firstComponent)
                            .child(
                                SolidColor.create(c).color(Color.GREEN))))
            .child(
                Container.create(c).flexDirection(YogaFlexDirection.COLUMN).flexShrink(0).alignContent(YogaAlign.FLEX_START)
                    .clickHandler(c.newEventHandler(2))
                    .child(
                        Text.create(c).text("test2")))
            .build();
      }
    };

    ComponentTree tree = ComponentTree.create(mContext, firstLayout)
        .incrementalMount(false)
        .build();
    ComponentView cv = new ComponentView(mContext);
    ComponentTestHelper.mountComponent(cv, tree);
    tree.setRoot(secondLayout);

    verify(mComponentsLogger).eventAddParam(
        EVENT_PREPARE_MOUNT,
        tree,
        PARAM_MOVED_COUNT,
        "2");
  }
}